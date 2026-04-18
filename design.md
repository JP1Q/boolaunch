# boolaunch
**Type:** Minimalistic TUI Android Launcher
**Status:** Open Source

## Core Philosophy: Maintainability
Code must be instantly understandable and optimized for human readability.
* **Hyper-Descriptive Naming:** Variables and functions must tell the exact story.
* **Single Responsibility Principle (SRP):** Functions should do exactly one thing.
* **Guard Clauses and Early Returns:** Use early returns to keep the "happy path" flat.
* **Magic Number Elimination:** Extract raw numbers and strings into well-named constants.
* **Strict Type Hinting:** Define exact input and return types to eliminate ambiguity.

## Colors

| Color Name | HEX | RGB | CMYK |
| :--- | :--- | :--- | :--- |
| Red (Accent) | #E11D48 | 225.29.72 | 0.87.68.12 |
| Dark Red | #9C0000 | 156.0.0 | 0.100.100.39 |
| Pale Red | #FFE1E2 | 255.225.226 | 0.12.11.0 |
| Deep Red (Background) | #510000 | 81.0.0 | 0.100.100.68 |
| White (Foreground) | #FFFFFF | 255.255.255 | 0.0.0.0 |

## Typography

### Heading Font
* **Font Family:** Funnel Display
* **Role:** Primary Logos / Large System Headers

### Body Font
* **Font Family:** Geist
* **Role:** Primary TUI interface text, interactive app lists

## Icons

* **Icon Set:** Akar Icons
* **Usage Note:** Primarily relying on ASCII characters (`+`, `-`, `[ ]`, `|`) for UI elements and touch targets. Akar Icons to be used strictly as minimal graphical fallbacks if required.

## TUI Elements & ASCII Art

### ASCII Logo

```text
 _                 _                       _      
| |__   ___   ___ | | __ _ _   _ _ __   ___| |__  
| '_ \ / _ \ / _ \| |/ _` | | | | '_ \ / __| '_ \ 
| |_) | (_) | (_) | | (_| | |_| | | | | (__| | | |
|_.__/ \___/ \___/|_|\__,_|\__,_|_| |_|\___|_| |_|
```

### Sample UI Layout

```text
================================================
[ boolaunch ]                 [ 100% ] [ 17:59 ]
================================================

  [ Phone ]

  [ Messages ]

  [ Browser ]

  [ Camera ]

  [ Settings ]

------------------------------------------------
           < swipe for more >            
================================================
```
```