using UnityEngine;
using System.Collections;

public class KillZone : MonoBehaviour {

	//private LivesCounter livesCounter;
	// Use this for initialization
	void Start () 
	{
		//livesCounter = GameObject.Find("LivesCount").GetComponent<LivesCounter>();
	}
	
	// Update is called once per frame
	void Update () 
	{
	
	}

	void OnTriggerEnter2D(Collider2D other)
	{
		if(other.gameObject.tag == "Player")
			print ("You died!");
		//Update the GUI
		//livesCounter.totalLives--;
	}
}
