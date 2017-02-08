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
