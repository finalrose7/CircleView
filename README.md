# CircleView #

A colorful circle view with text.

![license](https://img.shields.io/badge/license-Apache%202.0-blue.svg)
![maven-central](https://img.shields.io/badge/maven--central-1.0.0-brightgreen.svg)
![API](https://img.shields.io/badge/API-14%2B-green.svg)

A colorful circle view with text, you can use it as a avatar! Except in addition, we can set the direction of the text, vertical or horizontal and any angle.

## Screenshots ##

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
![demo_gif](/screenshots/demo_gif.gif) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
![in_rv](/screenshots/in_rv.png)
## Getting started ##

Add a dependency to your `build.gradle`:

Now migrated to mavenCentral !

```groovy
dependencies {
    implementation 'io.github.finalrose7:CircleView:1.0.0'
}
```

or Maven:

```
<dependency>
  <groupId>io.github.finalrose7</groupId>
  <artifactId>CircleView</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

## Usage ##

Add the `me.songning.library.CircleView` to your layout XML file.

```xml
    <me.songning.library.CircleView
        android:id="@+id/circleView"
        android:layout_width="88dp"
        android:layout_height="88dp"
        app:randomColor="true"
        app:text="Hello"
        app:textOrientation="vertical"/>
```

### XML attributes ###

| Name | Type | Default | Description |
|:----:|:----:|:-------:|:-----------:|
|circleColor|color|#03A9F4|CircleView background color|
|textColor|color|#FFFFFF|Text color|
|textSize|dimension|16sp|Text size|
|text|string|empty|Text content|
|randomColor|boolean|false|If true, the CircleView background color will show in random color|
|singleText|boolean|false|If true, no matter how long the text, just display a text|
|textAngle|float|0f|Set the text arbitrary angle in CircleView|
|speed|integer|4000|If it can rotate, set the rotation speed|
|textOrientation|enum|vertical|Text direction|
|rotateOrientation|enum|clockwise|Text rotation direction|

### Public Methods ###

| Name | Description |
|:----:|:-----------:|
|startRotateAnimation()|Start rotating|
|stopRotateAnimation()|Stop rotating|
|toggleRotateOrientation()|If rotating, toggle direction of rotation|
|setRotateOrientation(int)|Set rotation direction of the rotate animation, clockwise or anti-clockwise|
|setRotateSpeed(int)|Set rotation speed|
|setCircleColor(int)|Set CircleView background color|
|setTextColor(int)|Set text color|
|setTextSize(float)|Set text size|
|setText(int or String)|Set text content|
|setRandomColor(boolean)|If true, the CircleView background color will show in random color|
|setSingleText(boolean)|If true, no matter how long the text, just display a text|
|setAngle(float)|Set the text arbitrary angle in CircleView|
|setTextOrientation(int)|Set text direction, vertical or horizontal|
|show()|Show the CircleView with the scale animation|
|hide()|Hide the CircleView with the scale animation|
|toggleShow()|Toggle show or hide|

## Versions ##

### 1.0.0 ###
* Initial release

## About me ##

* Email: finalrose7@gmail.com

## License ##

```
Copyright 2016 Finalrose7

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
