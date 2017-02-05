using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

public class EventHandler : MonoBehaviour, IPointerClickHandler, IPointerEnterHandler, IPointerExitHandler, IPointerDownHandler, IDragHandler {

	public void OnPointerClick(PointerEventData eventData){
		if(eventData.pointerId == -1){
			Debug.Log ("Left Mouse Clicked.");
		} else if(eventData.pointerId == -2){
			Debug.Log ("Right Mouse Clicked.");
		}
	}

	public void OnPointerEnter(PointerEventData eventData){
		Debug.Log ("Pointer Enter..");
	}

	public void OnPointerExit(PointerEventData eventData){
		Debug.Log ("Pointer Exit..");
	}

	public void OnPointerDown(PointerEventData eventData){
		Debug.Log ("Pointer Down..");
	}

	public void OnDrag(PointerEventData eventData){
		Debug.Log ("Dragged..");
	}

}
