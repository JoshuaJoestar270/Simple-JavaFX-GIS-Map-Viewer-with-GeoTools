# Simple JavaFX GIS Map Viewer with GeoTools

2D vector map viewer application built with JavaFX and GeoTools.

### Overview
Created a desktop GIS viewer that loads and renders shapefiles as interactive vector maps.  
- Loaded Natural Earth countries boundary data (shapefile format)  
- Applied SLD styling for clean visualization (fill, outline)  
- Embedded the map in a JavaFX window using JMapPane + SwingNode  
- Added basic pan/zoom interaction (mouse drag + buttons)  

Showcases open-source GIS integration, shapefile handling, cartographic styling, and JavaFX desktop UI development.

### Tech Stack
- Java  
- JavaFX  
- GeoTools (gt-shapefile, gt-swing)  
- SwingNode  


### Setup Notes
- Download free shapefile from: https://www.naturalearthdata.com/downloads/110m-cultural-vectors/  
- Unzip to `data/ne_110m_admin_0_countries/` (do NOT commit to repo)  
- Add GeoTools JARs to classpath  
- Use JavaFX VM args: `--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.swing`

Skills demonstrated: GIS data handling, open-source library integration, Java desktop development, UI embedding.
