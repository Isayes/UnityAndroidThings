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
