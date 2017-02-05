using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TestEvent : MonoBehaviour {

	void Start () {
		Button btn = this.GetComponent<Button> ();
		UIEventListener btnListener = btn.gameObject.AddComponent<UIEventListener> ();

		btnListener.OnClick += delegate(GameObject gb) {
			Debug.Log(gb.name + " OnClick");
		};

		btnListener.OnMouseEnter += delegate(GameObject gb) {
			Debug.Log(gb.name + " OnMouseEnter");
		};

		btnListener.OnMouseExit += delegate(GameObject gb) {
			Debug.Log(gb.name + " OnMOuseExit");
		};
	}

}
