#!/bin/sh

# Script may take 6-7 command line parameters:
# $1: Hudson job name: <name>
# $2: Hudson build id: <id>
# $3: Build type: i(ntegration), m(aintenance), s(table), r(elease)
# $4: Whether to promote to an update-site: (y)es, (n)o
# $5: Whether to merge the site with an existing one: (y)es, (n)o
# $6: Whether to generate udpate-site and SDK drop files: (y)es, (n)o
# $7: An optional label to append to the version string when creating drop files, e.g. M5 or RC1
# 
if [ $# -eq 6 -o $# -eq 7 ];
        then
                jobName=$1
                buildId=$2
                buildType=$3
                site=$4
                merge=$5
                dropFiles=$6
                if [ -n "$7" ];
                then
                        dropFilesLabel=$8
                fi
        else
                if [ $# -ne 0 ];
                then
                        exit 1
                fi
fi

if [ -z "$jobName" ];
then
        echo -n "Please enter the name of the Hudson job you want to promote:"
        read jobName
fi

if [ -z "$buildId" ];
then
        for i in $( find /shared/jobs/$jobName/builds -type l | sed 's!.*/!!' | sort)
        do
                echo -n "$i, "
        done
        echo "lastStable, lastSuccessful"
        echo -n "Please enter the id/label of the Hudson build you want to promote:"
        read buildId
fi
if [ -z "$buildId" ];
        then
                exit 0
fi

# Determine the build we want to publish
if [ "$buildId" = "lastStable" -o "$buildId" = "lastSuccessful" ];
        then
                jobDir=$(readlink -f /shared/jobs/$jobName/$buildId)
        else
                jobDir=$(readlink -f /shared/jobs/$jobName/builds/$buildId)
fi
localUpdateSite=$jobDir/archive/update-site
echo "Using local update-site: $localUpdateSite"

# Reverse lookup the build id (in case lastSuccessful or lastStable was used)
for i in $(find /shared/jobs/$jobName/builds/ -type l)
do
        if [ "$(readlink -f $i)" =  "$jobDir" ];
                then
                        buildId=${i##*/}
        fi
done
echo "Reverse lookup of build id yielded: $buildId"

# Select the build type
if [ -z "$buildType" ];
then
        echo -n "Please select which type of build you want to publish to [i(ntegration), m(aintenance), s(table), r(elease)]: "
        read buildType
fi
echo "Publishing as $buildType build"

# check if we are going to promote to an update-site
if [ -z "$site" ];
        then
                echo -n "Do you want to promote to an remote update site? [(y)es, (n)o]:"
                read site
fi
if [ "$site" != y -a "$site" != n ];
        then
                exit 0
fi
echo "Promoting to remote update site: $site"

if [ "$site" = y ];
        then

  # Determine remote update site we want to promote to (integration and maintenance are published on interim site, stable builds on milestone site, release builds on releases site)
  case $buildType in
        i|I|m|M) remoteSite=interim;;
        s|S) remoteSite=milestones;;
        r|R) remoteSite=releases;;
        *) exit 0 ;;
  esac
  remoteUpdateSiteBase="tools/gef/updates/$remoteSite"
  remoteUpdateSite="/home/data/httpd/download.eclipse.org/$remoteUpdateSiteBase"
  echo "Publishing to remote update-site: $remoteUpdateSite"

  if [ -d "$remoteUpdateSite" ];
        then
                if [ -z "$merge" ];
                then
                        echo -n "Do you want to merge with the existing update-site? [(y)es, (n)o]:"
                        read merge
                fi
                if [ "$merge" != y -a "$merge" != n ];
                        then
                        exit 0
                fi
        else
                merge=n
  fi
  echo "Merging with existing site: $merge"
fi

# check if we are going to create drop files
if [ -z "$dropFiles" ];
        then
                echo -n "Do you want to create update-site and SDK drop files? [(y)es, (n)o]:"
                read dropFiles
fi
if [ "$dropFiles" != y -a "$dropFiles" != n ];
        then
                exit 0
fi
echo "Generating update-site and SDK drop files: $dropFiles"

if [ -z "$dropFilesLabel" -a "$dropFiles" = y ];
        then
                echo -n "Please enter a drop files label to append to the version (e.g. M5, RC1) or leave empty to skip this [<empty>]:"
                read dropFilesLabel
fi

# Prepare a temp directory
tmpDir="$jobName-publish-tmp"
rm -fr $tmpDir
mkdir -p $tmpDir/update-site
cd $tmpDir

# Download and prepare Eclipse SDK, which is needed to merge update site and postprocess repository 
echo "Downloading eclipse to $PWD"
cp /home/data/httpd/download.eclipse.org/eclipse/downloads/drops4/R-4.2.2-201302041200/eclipse-SDK-4.2.2-linux-gtk-x86_64.tar.gz .
tar -xvzf eclipse-SDK-4.2.2-linux-gtk-x86_64.tar.gz
cd eclipse
chmod 700 eclipse
cd ..
if [ ! -d "eclipse" ];
        then
                echo "Failed to download an Eclipse SDK, being needed for provisioning."
                exit
fi
# Prepare Eclipse SDK to provide WTP releng tools (used to postprocess repository, i.e set p2.mirrorsURL property)
echo "Installing WTP Releng tools"
./eclipse/eclipse -nosplash --launcher.suppressErrors -clean -debug -application org.eclipse.equinox.p2.director -repository http://download.eclipse.org/webtools/releng/repository/ -installIUs org.eclipse.wtp.releng.tools.feature.feature.group
# Clean up
echo "Cleaning up"
rm eclipse-SDK-4.2.2-linux-gtk-x86_64.tar.gz

# Prepare local update site (merging is performed later, if required)
cp -R $localUpdateSite/* update-site/
echo "Copied $localUpdateSite to local directory update-site."

# Generate drop files
if [ "$dropFiles" = y ];
        then
                echo "Converting update site to runnable form"
                ./eclipse/eclipse -nosplash -consoleLog -application org.eclipse.equinox.p2.repository.repo2runnable -source file:update-site -destination file:drops/eclipse
                qualifiedVersion=$(find drops/eclipse/features/ -maxdepth 1 | grep "org.eclipse.gef.all")
                qualifiedVersion=${qualifiedVersion#*_}
                qualifier=${qualifiedVersion##*.}
                version=${qualifiedVersion%.*}
                dropDir="$version/$(echo $buildType | tr '[:lower:]' '[:upper:]')$qualifier"
                localDropDir=drops/$dropDir
                echo "Creating drop files in local directory $localDropDir"
                mkdir -p $localDropDir
                cd drops
                
                # Append drop file suffix if one is specified                
                if [ -n "$dropFilesLabel" ];
                        then
                                version=$version$dropFilesLabel
                fi
                
                # GEF ALL
                zip -r $dropDir/GEF-ALL-$version.zip eclipse/features/* eclipse/plugins/*
                md5sum $dropDir/GEF-ALL-$version.zip > $dropDir/GEF-ALL-$version.zip.md5
                echo "Created GEF-ALL-$version.zip"
                
                # GEF SDK
                zip -r $dropDir/GEF-SDK-$version.zip eclipse/features/org.eclipse.draw2d_* eclipse/features/org.eclipse.draw2d.sdk_* eclipse/features/org.eclipse.draw2d.source_*\
                                                     eclipse/features/org.eclipse.gef_* eclipse/features/org.eclipse.gef.sdk_* eclipse/features/org.eclipse.gef.source_*\
                                                     eclipse/plugins/org.eclipse.draw2d_* eclipse/plugins/org.eclipse.draw2d.doc.isv_* eclipse/plugins/org.eclipse.draw2d.source_*\
                                                     eclipse/plugins/org.eclipse.gef_* eclipse/plugins/org.eclipse.gef.doc.isv_* eclipse/plugins/org.eclipse.gef.examples.ui.pde_* eclipse/plugins/org.eclipse.gef.source_*
                md5sum $dropDir/GEF-SDK-$version.zip > $dropDir/GEF-SDK-$version.zip.md5
                echo "Created GEF-SDK-$version.zip"
                
                # GEF runtime
                zip -r $dropDir/GEF-runtime-$version.zip eclipse/features/org.eclipse.draw2d_* eclipse/features/org.eclipse.gef_* \
                                                     eclipse/plugins/org.eclipse.draw2d_* eclipse/plugins/org.eclipse.gef_*
                md5sum $dropDir/GEF-runtime-$version.zip > $dropDir/GEF-runtime-$version.zip.md5
                echo "Created GEF-runtime-$version.zip"
                
                # GEF examples
                zip -r $dropDir/GEF-examples-$version.zip eclipse/features/org.eclipse.gef.examples_* eclipse/features/org.eclipse.gef.examples.source_* \
                                                     eclipse/plugins/org.eclipse.gef.examples.*
                md5sum $dropDir/GEF-examples-$version.zip > $dropDir/GEF-examples-$version.zip.md5
                echo "Created GEF-examples-$version.zip"
                
                 # Draw2d runtime
                zip -r $dropDir/GEF-draw2d-$version.zip eclipse/features/org.eclipse.draw2d_* eclipse/plugins/org.eclipse.draw2d_*
                md5sum $dropDir/GEF-draw2d-$version.zip > $dropDir/GEF-draw2d-$version.zip.md5
                echo "Created GEF-draw2d-$version.zip"
                
                # Draw2d SDK
                zip -r $dropDir/GEF-draw2d-sdk-$version.zip eclipse/features/org.eclipse.draw2d_* eclipse/features/org.eclipse.draw2d.source_* eclipse/features/org.eclipse.draw2d.sdk_*\
                                                     eclipse/plugins/org.eclipse.draw2d_* eclipse/plugins/org.eclipse.draw2d.doc.isv_* eclipse/plugins/org.eclipse.draw2d.source_*
                md5sum $dropDir/GEF-draw2d-sdk-$version.zip > $dropDir/GEF-draw2d-sdk-$version.zip.md5
                echo "Created GEF-draw2d-sdk-$version.zip"
                
                # Zest runtime
                zip -r $dropDir/GEF-zest-$version.zip eclipse/features/org.eclipse.draw2d_* eclipse/features/org.eclipse.zest_*\
                                                     eclipse/plugins/org.eclipse.draw2d_* eclipse/plugins/org.eclipse.zest.core_* eclipse/plugins/org.eclipse.zest.layouts_*
                md5sum $dropDir/GEF-zest-$version.zip > $dropDir/GEF-zest-$version.zip.md5
                echo "Created GEF-zest-$version.zip"
                
                # Zest SDK
                zip -r $dropDir/GEF-zest-sdk-$version.zip eclipse/features/org.eclipse.draw2d_* eclipse/features/org.eclipse.draw2d.source_* eclipse/features/org.eclipse.draw2d.sdk_*\
                										  eclipse/features/org.eclipse.zest_* eclipse/features/org.eclipse.zest.source_* eclipse/features/org.eclipse.zest.sdk_*\
                                                          eclipse/plugins/org.eclipse.draw2d_* eclipse/plugins/org.eclipse.draw2d.doc.isv_* eclipse/plugins/org.eclipse.draw2d.source_*\
                                                          eclipse/plugins/org.eclipse.zest.core_* eclipse/plugins/org.eclipse.zest.core.source_* eclipse/plugins/org.eclipse.zest.layouts_* eclipse/plugins/org.eclipse.zest.layouts.source_*
                md5sum $dropDir/GEF-zest-sdk-$version.zip > $dropDir/GEF-zest-sdk-$version.zip.md5
                echo "Created GEF-zest-sdk-$version.zip"
                
                cd ..
                cd update-site

                zip -r ../$localDropDir/GEF-Update-$version.zip features plugins artifacts.jar content.jar
                md5sum ../$localDropDir/GEF-Update-$version.zip > ../$localDropDir/GEF-Update-$version.zip.md5
                echo "Created GEF-Update-Site-$version.zip"
                cd ..

                #generating build.cfg file to be referenced from downloads web page
                echo "hudson.job.name=$jobName" > $localDropDir/build.cfg
                echo "hudson.job.id=$buildId (${jobDir##*/})" >> $localDropDir/build.cfg
                echo "hudson.job.url=https://hudson.eclipse.org/hudson/job/$jobName/$buildId" >> $localDropDir/build.cfg

                remoteDropDir=/home/data/httpd/download.eclipse.org/tools/gef/downloads/drops/$dropDir
                mkdir -p $remoteDropDir
                cp -R $localDropDir/* $remoteDropDir/
fi

if [ "$site" = y ];
        then
  if [ "$merge" = y ];
        then
        echo "Merging existing site into local one."
        ./eclipse/eclipse -nosplash --launcher.suppressErrors -clean -debug -application org.eclipse.equinox.p2.metadata.repository.mirrorApplication -source file:$remoteUpdateSite -destination file:update-site
        ./eclipse/eclipse -nosplash --launcher.suppressErrors -clean -debug -application org.eclipse.equinox.p2.artifact.repository.mirrorApplication -source file:$remoteUpdateSite -destination file:update-site
        echo "Merged $remoteUpdateSite into local directory update-site."
  fi

  # Ensure p2.mirrorURLs property is used in update site
  echo "Setting p2.mirrorsURL to http://www.eclipse.org/downloads/download.php?format=xml&file=/$remoteUpdateSiteBase"
./eclipse/eclipse -nosplash --launcher.suppressErrors -clean -debug -application org.eclipse.wtp.releng.tools.addRepoProperties -vmargs -DartifactRepoDirectory=$PWD/update-site -Dp2MirrorsURL="http://www.eclipse.org/downloads/download.php?format=xml&file=/$remoteUpdateSiteBase"

  # Create p2.index file
  if [ ! -e "update-site/p2.index" ];
        then
                echo "Creating p2.index file."
                echo "version = 1" > update-site/p2.index
                echo "metadata.repository.factory.order = content.xml,\!" >> update-site/p2.index
                echo "artifact.repository.factory.order = artifacts.xml,\!" >> update-site/p2.index
  fi

  # Backup then clean remote update site
  echo "Creating backup of remote update site."
  if [ -d "$remoteUpdateSite" ];
        then
                if [ -d BACKUP ];
                        then
                                rm -fr BACKUP
                fi
                mkdir BACKUP
                cp -R $remoteUpdateSite/* BACKUP/
                rm -fr $remoteUpdateSite
  fi

  echo "Publishing contents of local update-site directory to remote update site $remoteUpdateSite"
  mkdir -p $remoteUpdateSite
  cp -R update-site/* $remoteUpdateSite/
fi


# Clean up
echo "Cleaning up"
rm -fr eclipse
rm -fr update-site
