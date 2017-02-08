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
