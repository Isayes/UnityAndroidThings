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
