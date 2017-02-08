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
