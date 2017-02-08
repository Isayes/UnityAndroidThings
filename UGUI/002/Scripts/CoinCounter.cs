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
