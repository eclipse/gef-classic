# [GEF 3.10.1 (Mars SR1)](https://projects.eclipse.org/projects/tools.gef/releases/3.10.1-mars-sr1)

The GEF 3.10.1 (Mars SR1) release provides service revisions of the production components Draw2d 3.x (3.10.1), GEF (MVC) 3.x (3.10.1), and Zest 1.x (1.6.1), as well as minor revisions of the new GEF4 components (0.2.0).

The most significant changes related to the GEF4 components are documented in the GEF @ GitHub.com [CHANGELOG](https://github.com/eclipse/gef/blob/master/CHANGELOG.md).

# [GEF 3.10.0 (Mars)](https://projects.eclipse.org/projects/tools.gef/releases/3.10.0-mars)

The GEF 3.10.0 (Mars) release provides minor releases for the production components Draw2d 3.x (3.10.0), GEF (MVC) 3.x (3.10.0), and Zest 1.x (1.6.0), as well as a first release (0.1.0) of the new GEF4 components. It has to be pointed out that the Draw2d 3.x, GEF (MVC) 3.x, and Zest 1.x production components are not further developed but only maintained, while development is currently focussing the GEF4.

While GEF4 Common, GEF4 Geometry, GEF4 FX, and GEF4 MVC components have been mostly written from scratch, the GEF4 Graph, GEF4 Layout, GEF4 DOT, GEF4 Zest, and GEF4 Clodio components are (at least partially) based on the former Zest2 code base. However, in contrast to it, everything contributed to the 3.10.0 (Mars) release is completely self-contained, i.e. all components have been migrated/re-written so that none them relies on the API provided by Draw2d 3.x, GEF (MVC) 3.x, or Zest 1.x.

An overview of all GEF4 components and detailed documentation on each can be found under GEF4 reference documentation. The most significant changes in the Mars release timeframe are documented in the GEF @ GitHub.com [CHANGELOG](https://github.com/eclipse/gef/blob/master/CHANGELOG.md).

## Draw2d 3.10.0

Only minor changes have been applied to the Draw2d 3.x production component, which is in maintenance mode. 

### Tooltip Delay (3.10.0 M1)

TooltipHelper now allows clients to set the tooltip delay (see Bug [#323656](https://bugs.eclipse.org/bugs/show_bug.cgi?id=332351)).

## GEF (MVC) 3.10.0

Only minor changes have been applied to the GEF (MVC) 3.x production component, which is in maintenance mode.

### Redoing Commands (M2)

Added Command#canRedo(), which by default delegates to Command#canExecute() but can be overwritten to decide whether a command is redoable or not. The CommandStack now evaluates this when checking of the command on its top can be redone (CommandStack#canRedo()) (see Bug [#332351](https://bugs.eclipse.org/bugs/show_bug.cgi?id=332351)).

## Zest 1.6.0

Only minor changes have been applied to the Zest 1.x production component, which is in maintenance mode.
