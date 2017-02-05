using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

// 需要 EventTrigger 脚本的支援
[RequireComponent(typeof(UnityEngine.EventSystems.EventTrigger))]
public class EventTriggerHandler : MonoBehaviour {

	// Use this for initialization
	void Start () {
		Button btn = this.GetComponent<Button> ();
		EventTrigger trigger = btn.gameObject.GetComponent<EventTrigger> ();
		EventTrigger.Entry entry = new EventTrigger.Entry ();
		// 鼠标点击事件
		entry.eventID = EventTriggerType.PointerClick;
		// 鼠标进入事件 entry.eventID = EventTriggerType.PointerEnter;
		// 鼠标滑出事件 entry.eventID = EventTriggerType.PointerExit;
		entry.callback = new EventTrigger.TriggerEvent ();
		entry.callback.AddListener (OnClick);
		// entry.callback.AddListener (OnMouseEnter);
		trigger.triggers.Add (entry);
	}
	
	private void OnClick(BaseEventData pointData){
		Debug.Log ("Button Clicked. EventTrigger..");
	}

	private void OnMouseEnter(BaseEventData pointData){
		Debug.Log ("Button Enter. EventTrigger..");
	}
}
