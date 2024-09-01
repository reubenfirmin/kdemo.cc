# Kotlin.js + Tailwind + Kotlinx.html template

This is a template project which combines kotlin.js and kotlinx.html. This gives you a JSX-like way to build frontend UIs, without needing anything heavyweight
like React (that said, no performance testing has been done on this yet.)

## To Run Locally

`./gradlew watch`

This will open a browser tab which will hot reload on changes to code. Tailwind is automatically rerun when new styles are added.

## To Build For Production

`./gradlew clean build`

Deploy the contents of `build/dist/js/productionExecutable/`.

## Components

Check out the very simple `Badge.kt` component, and how it's used in the dsl in `Main.kt`.

