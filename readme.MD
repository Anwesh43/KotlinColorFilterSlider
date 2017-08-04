### ColorFilterSliderKotlin

#### A module to add list of color filters over an image coded entirely in Kotlin. One can choose a color by swiping left or right

### Usage 

#### Add module to the app. In app/build.gradle

```
	compile project(':colorfiltersliderkotlin')
```

#### Creating a colorfilterslider by supplying instance of current Activity, Image to which we want to apply colors and list of colors. In your Activity file

```
	ColorFilterSlider.create(this,bitmap, arrayOf(Color.GREEN,Color.CYAN,Color.BLACK,Color.BLUE,Color.RED,Color.MAGENTA))
```

### Demo

<img src="https://github.com/Anwesh43/KotlinColorFilterSlider/blob/master/demo/colorfilterslider.gif" alt="demo" width="300px" height="500px">
