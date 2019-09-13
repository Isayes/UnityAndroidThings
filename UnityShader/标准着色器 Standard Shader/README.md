# 对 Unity 5 标准着色器 Standard Shader 的调研

学习资料参考了：
- 冯乐乐《Unity Shader 入门精要》348 ~ 356 页
- <http://forum.china.unity3d.com/thread-897-1-1.html>
- <https://yq.aliyun.com/articles/10736>
- <http://blog.csdn.net/poem_qianmo/article/details/49556461>
- <https://docs.unity3d.com/Manual/shader-StandardShader.html>

学习笔记记于 2017-04-24

## 标准着色器的概念 # #

Unity 5 里面采用了一套全新的 Shader 叫做标准着色器（Standard Shader），这套 Shader 使用了基于物理的光照计算，而老版本的那些 Shader 已经被 Unity 不推荐使用了，Unity 5 认为我们大多数情况下都应该使用 Standard Shader。

Unity 5 包含了若干的 Standard Shaders，它们共同组成了一个完整的 PBS 光照明模型，且非常易于使用。

`PBS 的意思是 Physically Based Shading “基于物理的着色”，以某种方式模拟现实中材质和光照的相互作用。PBS 在需要光照和材质更加直观和逼真地协同工作的场合下优势非常明显，PBS 是创建一种友好方式来实现在不同光照条件下的逼真效果，模拟光线在现实中的行为，而不是使用多个特定的模型来模拟。为了实现这种效果它要遵循物理原理，包括：能量守恒，即物体反射出去的光量不可能超过所接收的光量；Fresnel 反射，即所有表面反射在掠射角处更加强烈；物体表面如何自我遮挡等；`

物理渲染和光照计算的最大区别就是：当今流行的 lambert blinn phone 基本上是基于模拟的模型，就是尽可能的去模拟我们看上去的物体发射的颜色，用于欺骗我们的眼睛；而基于物理的光照计算则是依据了光线传播的物体特性，更加贴近于真实的光照情况，虽然在实现上用了一些近似计算；所以物理渲染在表现自然界的物体上尤其看上去更加真实。

Unity 5 中目前有两个标准着色器，一个名为 Standard，我们称它为标准着色器的标准版，另一个名为 Standard（Specular Setup），我们称它为标准着色器的高光版，它们共同组成了一个完整的 PBS 光照明模型，且非常易于使用。

![](http://wx2.sinaimg.cn/mw690/a53846c3gy1fexuo9qkv0j20fo074dh8.jpg)

Standard Shader 主要针对硬质表面（也就是建筑材质）而设计的，可以处理大多数现实世界的材质，例如石头、陶瓷、铜器、银器或橡胶等，但它也可以非常出色的处理一些非硬质表面的材质，例如皮肤、头发或布料等；

![](https://docs.unity3d.com/uploads/Main/StandardShaderIntroVikingScene.png)

这套 Shader 的设计初衷是化繁为简。想用这样的一个多功能 Shader，来代替之前多种多样的 Shader 各司其职，对于不同的材质效果，需要不同的 Shader 的局面。

标准着色器引入了新的材质编辑器，它使 PBS 的材质编辑工作比以前的非 PBS 材质更简单。新的编辑器不但简洁，还提供了材质的所有可能用到的选项。在新编辑器中，我们不需要选用不同的着色器来改变纹理通道；不会再出现 “texture unused, please choose another shader” 这样的提示；也不再需要通过切换着色器来改变混合模式。下面是两个标准着色器的材质编辑器截图：

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fexuoa6f4oj20em0tqacu.jpg) . ![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fexuoatj20j20ei0tmdim.jpg)

其实这两个 Shader 基本差不多，只是有细微的属性参数上的区别。标准版这边的 `_Metallic`（金属性）、`_MetallicGlossMap`（金属光泽贴图），被高光版的 `_SpecColor`（高光颜色）、`_SpecGlossMap`（高光颜色法线贴图）所代替。

所有的纹理通道都是备选的，无需强制使用，任何一个闲置通道的相关代码都会在编译时被优化掉，因此完全不用担心效率方面的问题。Unity 会根据我们输入到编辑器中的数据来生成正确的代码，并使整个过程保持高效。

## 标准着色器的参数 # #

Albedo：物体表面的基本颜色，在物理模型中相当于物体表面某处各子表面的散射颜色。

Metallic：相当于物理模型中的 F(0)，即物体表面和视线一致的面的对光线反射的能量，通常金属物体通常超过 50%，大部分在 90%，而非金属集中在 20% 以下，自然界中的物质很少有在 20% - 40% 之间的（除非一些人造物体），正因为如此这个属性被形象的称谓 Metallic（金属感）

Smoonthness：相当于物理模型中与实现一致的面占所有微面的比例，比例越大，物体越光滑，反之越毛糙，要区分好这个和 Metallic 的区别（ Metallic 在描述对反射能量的强弱，Smoonthness 描述表面的光滑程度），当然大多数情况下金属的 Smoonthness 都很高。

Normal Map：法线贴图

Hight Map：视差贴图，用于在法线贴图的基础上表现高低信息（法线只能表现光照强弱，而视差贴图可以增加物理上的位置的前后）

Occlution：遮挡占据贴图，用于模拟 GI，物体在默写凹槽处由于受到光线的减少而显得暗，也就是自遮挡。

Emission：自发光，不过 Unity 5 的自发光可以在全局光照中当光源使用，非常好

Secondary Maps：第二道贴图

Detail Mask：对第二道贴图的 mask

## 标准着色器的组成 # #

标准着色器作为一个着色器，由两个功能稍微复杂全面一些的 Shader 源文件，加上一堆 CG 头文件组成。其中，两个 Shader 源文件里，又按渲染路径分为了很多的 SubShader，每个 SubShader 里面又分为了很多 Pass。而 CG 文件中，主要包含了 Shader 的支持函数，相关的宏等为 Shader 源文件提供支持的代码。

简单描述就是：

2 个 Shader 源文件 +
7 个 CG 头文件 +
1 个自定义材质编辑器 UI 的脚本文件

### 2 个 Shader 源文件

`Stardard.shader` 着色器源文件 - 标准着色器的标准版
`StardardSpecular.shader` 着色器源文件 - 标准着色器的高光版

![](http://wx2.sinaimg.cn/mw690/a53846c3gy1fexuobfq77j20m603ct9k.jpg)

[源码在此](https://github.com/Isayes/Shaders/tree/master/%E6%A0%87%E5%87%86%E7%9D%80%E8%89%B2%E5%99%A8%20Standard%20Shader/2%20%E4%B8%AA%20Shader%20%E6%BA%90%E6%96%87%E4%BB%B6)

### 9 个 CG 头文件

`UnityStandardBRDF.cginc` 用于存放标准着色器处理 BRDF 材质属性相关的函数与宏。实现了 Unity 中基于物理的渲染技术，定义了 BRDF1_Unity_PBS、BRDF2_Unity_PBS 和 BRDF3_Unity_PBS 等函数，来实现不同平台下的 BRDF；

`UnityStandardConfig.cginc` 用于存放标准着色器配置相关的代码（其实里面就几个宏）。对 Standard Shader 的相关配置，例如默认情况下关闭简化版的 PBS 实现（将 UNITY_STANDARD_SIMPLE 设为 0），以及使用基于归一化的 Blinn-Phong 模型而非 GGX 模型来实现 BRDF（将 UNITY_BRDF_GGX 设为 0）

`UnityStandardCore.cginc` 用于存放标准着色器的主要代码（如顶点着色函数、片段着色函数等相关函数）。定义了 Standard 和 Standard(Specular setup) Shader 使用的顶点/片元着色器和相关的结构体、辅助函数等，如 vertForwardBase、fragForwardBase、MetallicSetup、SpecularSetup 函数和 VertexOutputForwardBase、FragmentCommonData 结构体；

`UnityStandardCoreForward.cgnic`
`UnityStandardCoreForwardSimple.cgnic`

`UnityStandardInput.cginc` 用于存放标准着色器输入结构相关的工具函数与宏。声明了 Standard Shader 使用的相关输入，包括 Shader 使用的属性和顶点着色器的输入结构体 VertexInput，并定义了基于这些输入的辅助函数，如 TextCoords、Albedo、Occlusion、SpecularGloss 等函数；

`UnityStandardMeta.cginc` 用于存放标准着色器 Meta 通道中会用到的工具函数与宏。定义了 Standard Shader 中 “LightMode” 为 “Meta” 的 Pass（用于提取光照纹理和全局光照的相关信息）使用的顶点/片元着色器，以及它们使用的输入/输出结构体；

`UnityStandardShadow.cginc` 用于存放标准着色器阴影贴图采样相关的工具函数与宏。定义了 Standard Shader 中 “LightMode” 为 “ShadowCaster” 的 Pass（用于投射阴影）使用的顶点/片元着色器，以及它们使用的输入/输出结构体；

`UnityStandardUtils.cginc` 用于存放标准着色器共用的一些工具函数。Standard Shader 使用的一些辅助函数，将来可能会移动到 UnityCG.cgnic 文件中；

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fexuoc5gy5j20gc09kjsx.jpg)

[源码在此](https://github.com/Isayes/Shaders/tree/master/%E6%A0%87%E5%87%86%E7%9D%80%E8%89%B2%E5%99%A8%20Standard%20Shader/9%20%E4%B8%AA%20CG%20%E5%A4%B4%E6%96%87%E4%BB%B6)

## 1 个脚本文件

`StandardShaderGUI.cs` 定义了特定的自定义编辑器 UI 界面

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fexuocpsxrj20m205c0tl.jpg)

[源码在此](https://github.com/Isayes/Shaders/blob/master/%E6%A0%87%E5%87%86%E7%9D%80%E8%89%B2%E5%99%A8%20Standard%20Shader/1%20%E4%B8%AA%E8%84%9A%E6%9C%AC%E6%96%87%E4%BB%B6/StandardShaderGUI.cs)

标准着色器对应材质的编辑器外观不同于一般的 Shader，就是因为在 Shader 末尾书写了如下的代码：

```csharp
//使用特定的自定义编辑器 UI 界面  
CustomEditor "StandardShaderGUI"
```

End.

PS：关于 PBS 技术，知乎上有一个专栏，专门介绍 PBS 技术的一些相关原理:

- [基于物理着色（一）](http://zhuanlan.zhihu.com/graphics/20091064)
- [基于物理着色（二）Microfacet 材质和多层材质](http://zhuanlan.zhihu.com/graphics/20119162)
- [基于物理着色（三）Disney 和 UE4 的实现](http://zhuanlan.zhihu.com/graphics/20122884)
