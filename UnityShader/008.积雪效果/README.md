# 积雪效果

为游戏中的所有纹理都加上雪花可能需要花费大量时间。本文将展示在 Unity 中如何创建 Image Effect（屏幕空间着色器）来快速改变场景的季节。

![](http://mmbiz.qpic.cn/mmbiz_png/TEibQpFBfnWLUiaQV33kF1eyHYUEXhpwBhgI9syrF1u0wlOTibTNT51eNrIqiaAhdxqeW6VbVQQxmbX0YD0a6cpYPQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

![](http://mmbiz.qpic.cn/mmbiz_png/TEibQpFBfnWLUiaQV33kF1eyHYUEXhpwBhps0UAwk05YNlhBMPjcma6XOnJ4Zfzh4zO7picocXEBbkbG0ic4Zm8SiaA/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

## 1. 工作原理

假定所有法线朝上的像素点（如：草，地板等）都需要覆盖雪花。同样，法线朝着其它方向的像素点(如：松树，墙)，则需要在雪花纹理和原始纹理之间进行平缓过渡。

## 2. 获取所需的数据

实现上面的雪花效果有以下准备事项：

- 将渲染路径设置为 Deferred（延迟渲染）
- 将 Camera.depthTextureMode 设置为 DepthNormals

由于第二项可以很方便地由屏幕特效脚本进行设置，所以如果游戏已经使用了前向渲染路径（Forward Rendering Path）时，第一项很容易出问题。将 Camera.depthTextureMode 设置为 DepthNormals 后可以读取屏幕深度（像素与相机之间的距离）和法线（所朝的方向）。

创建一个屏幕特效（Image Effect）由至少一个脚本和一个着色器构成。通常这个着色器不是用来渲染 3D 物体的，而是根据给定的输入数据渲染一个全屏的图像。在本文的例子中，输入数据就是一张相机渲染的结果图片以及一些用户设置的属性。

这里只是基础的设置，还不能生成雪花。

## 3. 着色器

雪花着色器是无光照着色器（unlit shader），因为屏幕空间是没有光照的，所以也不会用到任何光照信息。片段着色器才是重要部分，通过 ScreenSpaceSnow 脚本来获取所有数据。

## 4. 找出需要下雪的地方

正如之前所说，所有法线朝上的表面都将覆盖雪。相机已经设置了生成深度法线贴图，所以现在直接获取即可。

查看 Unity 官方文档可以了解 `_CameraDepthNormalsTexture` 的意义：
深度贴图可以作为一个着色器的全局着色器属性进行采样。通过声明名为 `_CameraDepthTexture` 的采样器，就能够采样相机的主深度纹理。
`_CameraDepthTexture` 总是引用相机的主深度贴图。

Unity 文档解释深度和法线的数据都打包为 16 位。所以获取法线需要调用 DecodeDepthNormal 方法进行解包。这个方法检索的是相机空间的法线。也就是说，如果旋转屏幕相机，那么法线朝向也会改变。脚本中将法线乘以 `_CamToWorld` 矩阵就是为了避免这种情况。它会将法线从相机空间转换为世界空间，这样就不再依赖于相机的透视。

![](http://mmbiz.qpic.cn/mmbiz_png/TEibQpFBfnWLUiaQV33kF1eyHYUEXhpwBhPs3uWpVz91XCQfEibzLR54MBibUl1gtDDCsibq9JG31D7SOviaOfo3bZ0Q/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

暂时渲染为 RGB 图像。在 Unity 中，Y 轴是默认向上的。图中绿色部分表示 Y 坐标轴的值。目前为止结果不错！接下来配置积雪覆盖区域顶部和底部的阀值，以便于调整场景的积雪量。

![](http://mmbiz.qpic.cn/mmbiz_png/TEibQpFBfnWLUiaQV33kF1eyHYUEXhpwBhxF8eAXgdSfE8HuaBDAbR40sachvbkgibBOYtFzwuNwUbiaFMAe0LTn4g/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

## 5. 雪纹理

如果没有纹理，雪看起来会不真实。最难的部分就是将 2D 纹理（屏幕空间）应用到 3D 物体上。一种方法是获取像素的世界坐标，然后将世界坐标的 X 和 Z 值作为纹理坐标。

这里涉及到一些数学知识，您只需知道 vpos 是视口坐标，wpos 是由视口坐标与 `_CamToWorld` 矩阵相乘而得到的世界坐标，并且它通过除以远平面的位置（`_ProjectionParams.z`）来转换为有效的世界坐标。最后使用 XZ 坐标乘以可配置参数 `_SnowTexScale` 和远平面，来计算雪的颜色并获取适当的值。

![](http://mmbiz.qpic.cn/mmbiz_png/TEibQpFBfnWLUiaQV33kF1eyHYUEXhpwBhRVw8jXAwZI2diaearmvUAkTFsEye2NgZ3eF38RGVJNH6yS8AtXYgupg/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

## 6. 合并

下面将积雪与场景进行合并。获取场景原始颜色，并使用 snowAmount 进行插值渐变为 snowColor 。

![](http://mmbiz.qpic.cn/mmbiz_png/TEibQpFBfnWLUiaQV33kF1eyHYUEXhpwBhgWKI3Qh2R4vbh7WiaHNBxfr8ibibibsVoiacRfcrNsDze8rVL7ozWN9WZtw/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

最后一步：将 `_TopThreshold` 设为0.6

![](http://mmbiz.qpic.cn/mmbiz_png/TEibQpFBfnWLUiaQV33kF1eyHYUEXhpwBhTAmr7vnZzNtMQAeZicpRhgvFBNfFyYEmyyYmZEI6TBVhgs4aB96SVhw/640?wx_fmt=png&wxfrom=5&wx_lazy=1)

End.
