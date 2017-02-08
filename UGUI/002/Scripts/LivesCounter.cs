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
