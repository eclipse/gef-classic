#!/bin/sh

jobName="gef-nightly-tycho"

# Script may take 4 command line parameters:
# $1: Hudson build id: <id>
# $2: Build type: n(ightly), m(ilestone), r(elease)
# $3: Whether to merge the site with an existing one: (y)es, (n)o
# $4: Whether to generate udpate-site and SDK drop files: (y)es, (n)o
# 
if [ $# -eq 4 ];
        then
                buildId=$1
                buildType=$2
                merge=$3
                dropFiles=$4
        else
                if [ $# -ne 0 ];
                then
                        exit 1
                fi
fi

if [ -z "$buildId" ];
then
        for i in $( find /shared/jobs/$jobName/builds -type l | sed 's!.*/!!' | sort)
        do
                echo -n "$i, "
        done
        echo "lastStable, lastSuccessful"
        echo -n "Please enter the id/label of the Hudson job you want to promote:"
        read buildId
fi
if [ -z "$buildId" ];
        then
                exit 0
fi

# Determine the local update site we want to publish to
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
echo "Reverse lookup yielded build id: $buildId"

# Select the build type
if [ -z "$buildType" ];
then
        echo -n "Please select which type of build you want to publish to [i(nterim), m(ilestone), r(elease)]: "
        read buildType
fi
echo "Publishing as $buildType build"

# Determine remote update site we want to promote to
case $buildType in
        i|I) remoteSite=interim ;;
        m|M) remoteSite=milestones ;;
        r|R) remoteSite=releases ;;
        *) exit 0 ;;
esac
remoteUpdateSite="/home/data/httpd/download.eclipse.org/tools/gef/updates/$remoteSite"
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

# Prepare a temp directory
tmpDir="$jobName-publish-tmp"
rm -fr $tmpDir
mkdir -p $tmpDir/update-site
cd $tmpDir

# Download Eclipse SDK, which is needed to merge update site or prepare drop files
if [ "$merge" = y -o "$dropFiles" = y ];
        then
                echo "Downloading eclipse to $PWD"
                cp /home/data/httpd/download.eclipse.org/eclipse/downloads/drops/R-3.7.1-201109091335/eclipse-SDK-3.7.1-linux-gtk-x86_64.tar.gz .
                tar -xvzf eclipse-SDK-3.7.1-linux-gtk-x86_64.tar.gz
                cd eclipse
                chmod 700 eclipse
                cd ..
fi

# Prepare local update site (merge if required)
if [ "$merge" = y ];
        then
        echo "Merging existing site into local one."
        ./eclipse/eclipse -nosplash -consoleLog -application org.eclipse.equinox.p2.metadata.repository.mirrorApplication -source file:$remoteUpdateSite -destination file:update-site
        ./eclipse/eclipse -nosplash -consoleLog -application org.eclipse.equinox.p2.metadata.repository.mirrorApplication -source file:$localUpdateSite -destination file:update-site
        ./eclipse/eclipse -nosplash -consoleLog -application org.eclipse.equinox.p2.artifact.repository.mirrorApplication -source file:$remoteUpdateSite -destination file:update-site
        ./eclipse/eclipse -nosplash -consoleLog -application org.eclipse.equinox.p2.artifact.repository.mirrorApplication -source file:$localUpdateSite -destination file:update-site
        echo "Merged $localUpdateSite and $remoteUpdateSite into local directory update-site."
else
        echo "Skipping merge operation."
        cp -R $localUpdateSite/* update-site/
        echo "Copied $localUpdateSite to local directory update-site."
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
                
                # GEF ALL
                zip -r $dropDir/GEF-ALL-$version.zip eclipse
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
                                                     eclipse/plugins/org.eclipse.examples.*
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
                mv update-site GEF-Update-$version
                zip -r $localDropDir/GEF-Update-$version.zip GEF-Update-$version
                md5sum $localDropDir/GEF-Update-$version.zip > $localDropDir/GEF-Update-$version.zip.md5
                echo "Created GEF-Update-Site-$version.zip"

                #generating build.cfg file to be referenced from downloads web page
                echo "hudson.job.name=$jobName" > $localDropDir/build.cfg
                echo "hudson.job.id=$buildId (${jobDir##*/})" >> $localDropDir/build.cfg
                echo "hudson.job.url=https://hudson.eclipse.org/hudson/job/$jobName/$buildId" >> $localDropDir/build.cfg

                remoteDropDir=/home/data/httpd/download.eclipse.org/tools/gef/downloads/drops/$dropDir
                mkdir -p $remoteDropDir
                cp -R $localDropDir/* $remoteDropDir/
fi