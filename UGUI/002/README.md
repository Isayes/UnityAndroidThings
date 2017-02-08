# 实现僵尸跑酷游戏的 UGUI 实践

博客园同步 :  <http://www.cnblogs.com/isayes/p/6380457.html>

## 0. 对本次实践的介绍 # #

学习 Unity 5.x 原生态 UGUI 的开发实践, 学习参考自 58 开发网, 这是一个跑酷类游戏的 UI 界面, 包含了游戏开始的 主界面|主角的 UI 设置|时间 UI 设置以及所获得的金币 UI 设置等. 通过此次实践可以了解到以下内容, 对 UGUI 有一个深入的认识 :

- Sprite 资源导入及切割
- 制作主界面欢迎文字
- 制作主界面开始|退出按钮
- 制作人物 HUD 和金币 HUD 和时间 HUD
- CanvasGroup 的使用
- UI 动画制作及显示
- 实现金币 UI 数量显示功能
- 实现生命数量 UI 更新功能
- 实现时间倒计时功能
- UI 自适应调整

想要实现的效果

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fcj75b32hqg20gr08xe81.gif?_=6380457)

## 1. Sprite 资源导入及切割 # #

UISprites.psd

![](http://wx4.sinaimg.cn/mw690/a53846c3gy1fchz4g4hjnj20o20ds0un.jpg)

把准备好的素材 UISprites.psd 导入项目, 检查 `Texture Type` 的类型是 `Sprite(2D and UI)` 模式, `Sprite Mode` 改成 `Multiple`, 进而 `Apply` 一下.

![](http://wx2.sinaimg.cn/mw690/a53846c3gy1fchz4hfi93j20l40hg76o.jpg)

点击进入 `Sprite Editor` 精灵的编辑器.

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fchz4h0m9mj20m80hyae6.jpg)

在 Sprite Editor 左键圈住内容选择切割区域, `Apply` 一下便切割开了.

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fchz4ijekfj20vi0kq7ag.jpg)

切割之前与切割之后的项目目录对比 :

![](http://wx2.sinaimg.cn/mw690/a53846c3gy1fchz4ghk8lj20cu06ijrs.jpg) 和 ![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fchz4hz1l5j20cu0bwwfl.jpg)

## 2. 制作主界面欢迎文字 # #

新建一个 `Canvas>Image`, 把切好的 UISprites 的 Title 作为 `Source Image` 给它. 调整大小和位置.

修改文字锚点的位置, 保持四个角的相对位置, 使之自适应屏幕大小的改变.

![](http://wx2.sinaimg.cn/mw690/a53846c3gy1fchz4jxzboj20r40aaab6.jpg)  

改成

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fchz4j13jyj20rk0a4gmt.jpg)

在 `Image` Title 下添加文字 `Text`, 修改内容和字体等格式, 选中 `Best Fit`, 同样, 也需要修改其锚点.

欢迎界面就做好了.

![](http://wx4.sinaimg.cn/mw690/a53846c3gy1fchz4kb1bfj20og0q8tcr.jpg)

## 3. 制作主界面开始|退出按钮 # #

`Canvas` 下面创建3个 `Button`, 分别对应为 `Start` | `Options` | `Exit`, 关联 `Source Image` 以及修改背景颜色和文字

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fchz4l0pqlj20c007et8y.jpg)

界面效果

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fchz4llz8zj20ug0skn2s.jpg)

## 4. 制作人物 HUD 和金币 HUD 和时间 HUD# #

添加一个新的 Canvas 里面有一个 Image 和两个 Text, 如下图所示

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fcidv0r2foj20ka0ci0tc.jpg)

为了适应屏幕, 将 Image 的 Rect Transform 设置为 `left + top`

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fcidtgcm9gj208407odg5.jpg)

Coin 的图标同理, 锚点设为 `right + top`.

新建 `Create Empty` 添加时间轴图片, 操作同上, 最终效果

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fcidv4dzvrj20m00e00tt.jpg)


## 5. CanvasGroup 的使用 # #

在 `MainMenuCanvas` 里新建一个 `Create Empty` 作为其他对象的父物体, 即 `Title StartBtn OptionsBtn ExitBtn` 都属于同一个 GameObject 命名为 `MainMenu`, 这个 MainMenu 的锚点设为 `Stretch - Stretch`.

为 `MainMenu` Add Component 添加 CanvasGroup 组件, 可以通过控制他的 Alpha 通道来显示或者隐藏界面

同理, 把原先的 MainMenuCanvas 重命名为 Canvas, 给其增加一个 HUD 用于放置原先的 Canvas 的子物体.增加 CanvasGroup, 可以通过控制他的 Alpha 通道来显示或者隐藏界面

上述描述的结果如下图所示

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fcidw7o140j20cu09udgf.jpg)

MainMenu 的 `CanvasGroup>Alpha` 初始化为 1, HUD 的 `CanvasGroup>Alpha` 初始化为 0.

给 StartBtn 按钮的 `On Click()` 添加事件通知 MainMenu 上面的 Alpha 属性值置为 0, 再添加一个事件通知 HUD 的 Alpha 属性值置为 1.

![](http://wx2.sinaimg.cn/mw690/a53846c3gy1fcidwu7cmqj20u40eqq87.jpg)

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fcidwybupjj20qy07ejs4.jpg)

动图效果

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fcidr2l9qjg20gz08lwjd.gif)

## 6. UI 动画制作及显示 # #

但是界面的切换有点生硬, 最好使用一下 UI 的动画制作, 使显示的更自然.

打开 `Window>Animation` 编辑器, 为 MainMenu 创建一个 Animator 和 一个 `Animation Clip`. Add Property `MainMenu : Canvas Group.Alpha` 范围设为动画的`0~1`对应其`1~0`

![](http://wx2.sinaimg.cn/mw690/a53846c3gy1fcj1202wj5g20ig0460ur.gif)

默认的 `Animator` 设置为只动画一次, 不要循环 : 把 `MainMenuFadeOut` 的 `Loop Time` 的勾选去掉.

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fcj1209s4qj20xg0cognk.jpg)

实现 HUD 的动画操作类似.

为了有更好的衔接效果, 最好给 Animator 里做个过渡, 即可以添加一个 `Create Empty New State` 作为默认的 `Default State` 再由它 `Make Transition` 到原有的 `MainMenuFadeOut`. HUD 操作同理

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fcj120dkepj21040hy76z.jpg)

触发相关 Trigger 的过程如下, 点击 Start 按钮触发 MainMenu 的 `FadeOut` 使之淡出, 紧接着也触发 HUD 的 `FadeIn` 使之淡入, 这样就做好了. 对 Trigger 的控制需要脚本. 脚本内容如下

**脚本 MainMenuController**

```csharp
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MainMenuController : MonoBehaviour {

	private Animator mainMenuAnim;

	void Awake(){
		mainMenuAnim = GetComponent<Animator> ();
	}

	public void MenuFade(){
		mainMenuAnim.SetTrigger ("FadeOut");
	}

}

```
**脚本 HudController**

```csharp
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HudController : MonoBehaviour {

	private Animator hudAnim;

	void Awake(){
		hudAnim = GetComponent<Animator> ();
	}

	public void HudFade(){
		hudAnim.SetTrigger ("FadeIn");
	}

}

```

让 `StartBtn` 删除原来的事件, 换成与上述脚本绑定的 `OnClick()` 事件

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fcj1216d9fj20ic0a0js8.jpg)

至此, 渐出渐入的动画效果就做好了. 效果如下所示 :

![](http://wx4.sinaimg.cn/mw690/a53846c3gy1fcj11n8do9g20ig0fph4k.gif)

## 7. 实现金币 UI 数量显示功能 # #

实现界面右上角金币的数量逻辑.

找到 `HUD>CoinsUI>TexCoinsCount` 添加一个 CoinCounter.cs 脚本.

僵尸吃硬币就是僵尸与金币进行碰撞, 每吃一个硬币就使右上角加1. 硬币 Coin 脚本 :

```csharp
using UnityEngine;
using System.Collections;

public class Coin : MonoBehaviour
{
	private CoinCounter coinCounter;

	void Awake()
	{
		coinCounter = GameObject.FindGameObjectWithTag ("TextCoinsCount").GetComponent<CoinCounter> ();
	}

	void OnTriggerEnter2D(Collider2D other)
	{
		if(other.gameObject.tag == "Player")
			print ("You picked up a coin!");

		coinCounter.coinCount++;
		gameObject.SetActive(false);
	}

}

```

**脚本 CoinCounter**

```csharp
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class CoinCounter : MonoBehaviour {

	public int coinCount = 0;

	void Start () {

	}

	void Update () {
		GetComponent<Text>().text = coinCount.ToString ();
	}
}

```

效果如图所示

![](http://wx3.sinaimg.cn/mw690/a53846c3gy1fcj3rz58khg20ig08d4qp.gif)

## 8. 实现生命数量 UI 更新功能 # #
硬币收集到一定数量就增加一个生命值的数量, 当收满 24 个金币的时候就会增加一条命.

![](http://wx4.sinaimg.cn/mw690/a53846c3gy1fcj75bodt3j211q0jegq5.jpg)

**脚本 LivesCounter**

```csharp
using UnityEngine;
using UnityEngine.UI;
using System.Collections;
[ExecuteInEditMode]

public class LivesCounter : MonoBehaviour
{
	public int initialLives = 3; // 游戏刚开始的时候的生命数量
	public int extraLives = 0; // 游戏进行时额外增加的生命数量
	public int totalLives; // 主角生命数量的总数

	void Start()
	{
		GetLives();
	}

	// Update is called once per frame
	void Update ()
	{
		totalLives = initialLives + extraLives;
		GetComponent<Text> ().text = totalLives.ToString ();
	}

	void GetLives()
	{
		totalLives = initialLives + extraLives;
	}
}

```
**脚本 GameState**

```csharp
using UnityEngine;
using UnityEngine.UI;
using System.Collections;

public class GameState : MonoBehaviour
{
	private GameObject[] coins;
	public int totalCoins; // 所有的金币总数

	public bool gameRunning = false;

	private CoinCounter coinCounter;
	private LivesCounter liveCounter;

	void Awake ()
	{
		coinCounter = GameObject.FindGameObjectWithTag ("TextCoinsCount").GetComponent<CoinCounter> ();
		liveCounter = GameObject.FindGameObjectWithTag ("TextLiveCount").GetComponent<LivesCounter> ();

		coins = GameObject.FindGameObjectsWithTag("Coin");
		totalCoins = coins.Length;
	}

	void Update ()
	{
		int collectedCoins;
		collectedCoins = coinCounter.coinCount; // 当前收集到的金币数量
		liveCounter.extraLives = collectedCoins / totalCoins;
		if (liveCounter.totalLives < 0) {
			print ("Game Over!");
		}
	}

	public void StartGame()
	{
		gameRunning = true;
	}

	public void GameOver()
	{
		gameRunning = false;
		print ("Game Over!");
	}
}

```

## 9. 实现时间倒计时功能 # #

也就是界面最上方中间的时间计数器的逻辑实现.

随着时间的流逝, 上面的颜色池减少. (图片由右向左的长度的减少), 把 `TimerFillImage` 的 `Image Type` 换成 Filled, 并且将 `Fill Method` 设置为 Horizontal, 那么 `Fill Amount` 就可以用来做计时了.

为 `Canvas>HUD>Timer>TimerFillImage` 新建脚本 Timer.cs

```csharp
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Timer : MonoBehaviour {

	public float currentTimer;
	public float startTimer = 10f;
	public float timerPercent;

	private Image image;

	void Awake () {
		currentTimer = startTimer;
		image = GetComponent<Image> ();
	}

	void Update () {
		// 计时
		currentTimer -= Time.deltaTime;
		timerPercent = currentTimer / startTimer;
		image.fillAmount = timerPercent;
	}
}

```

效果如图所示

![](http://wx4.sinaimg.cn/mw690/a53846c3gy1fcj756frtjg20bj03h0sx.gif)

但是有一点需要注意, 那就是只有当 StartBtn 被点击出现新界面之后才应该开始计时. 需要利用在 GameState.cs 里的 gameRunning 状态, 在 Timer.cs 中把 gameRunning 读取过来, 是否开始进行一下控制. 当然,还不能忘记了, 还需要给 StartBtn 再增加一个 点击事件的反馈, 即去调用 GameState.cs 里的 StartGame() 方法.

![](http://wx2.sinaimg.cn/mw690/a53846c3gy1fcj75coy2cj20l80a03zj.jpg)

看看 Timer.cs 脚本的改进

```csharp
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Timer : MonoBehaviour {

	public float currentTimer;
	public float startTimer = 10f;
	public float timerPercent;

	private Image image;
	private GameState gameState;

	void Awake () {
		currentTimer = startTimer;
		image = GetComponent<Image> ();
		gameState = GameObject.Find ("GameState").GetComponent<GameState> ();
	}

	void Update () {
		// 游戏的确开始在运行的时候才开始进行倒计时
		if(gameState.gameRunning){
			// 计时
			currentTimer -= Time.deltaTime;
			timerPercent = currentTimer / startTimer;
			image.fillAmount = timerPercent;
		}

	}
}

```

目前为止的效果

![](http://wx1.sinaimg.cn/mw690/a53846c3gy1fcj75b32hqg20gr08xe81.gif)

## 10. UI 自适应调整 # #

即需要调整锚点的地方就调整一下.

---

`学习自 58 开发网视频教程 : http://www.58kaifa.com/course/35`

End.

















End.
