# DashedCircularProgressDemo
DashedCircularProgressDemo 圆形齿轮进度

###### 最近做的项目里面用到了圆形齿轮进度的一个效果，在网上找了一下，看有没有已经是现实的效果，果然被我找到了。How lucky! 感谢[作者](https://github.com/Daemon1993/healthycricleviewdemo)造好的轮子，但是离我的需求还有一点点距离，我在基础上又修改了添加了一些，来实现项目需求。

话不多说，先上效果图</br>
以下是连续增加的效果</br>
![image](https://github.com/zhaixiaoou/DashedCircularProgressDemo/blob/master/screenshots/dashedcircularprogress.gif)</br>
隐藏里面圆环效果：</br>
![image](https://github.com/zhaixiaoou/DashedCircularProgressDemo/blob/master/screenshots/IMG_20161217_003531.jpg)</br>
隐藏小圆点效果：</br>
![image](https://github.com/zhaixiaoou/DashedCircularProgressDemo/blob/master/screenshots/IMG_20161217_003628.jpg)</br>
隐藏里面圆环进度：</br>
![image](https://github.com/zhaixiaoou/DashedCircularProgressDemo/blob/master/screenshots/IMG_20161217_003708.jpg)</br>

### 属性
***
有一些属性，大家可以自定义

| attr | 描述  |默认值|
|:---|:---:|:---:|
|base_color| 圆环底色|#444444|
|progress_color| 进度显示的颜色|#20af60|
|circle_progress| 是否显示内环的进度 | true|
|inner_circle| 是否显示内环|true|
|max| 进度最大值|100|
|min|进度最小值|0|
|duration|动画的时间间隔|1000|
|progress_stroke_width| 虚线的长度|20px|
|inner_circle_width|内环的半径|外环半径-50px|
|is_animation|进度是否显示动画|true|
|dash_count|虚线总共显示的个数|60|
|circle_stroke_width|内环的描线的宽度|5px|
|head_radius|内环小圆点的半径|10px|
|is_head|内环是否显示小圆点|true|

### 备注
***
希望可以帮助你！有什么问题欢迎提Issues.O(∩_∩)O谢谢