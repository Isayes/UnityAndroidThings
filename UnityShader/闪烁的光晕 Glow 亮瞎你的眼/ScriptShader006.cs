using UnityEngine;
using System.Collections;

public class ScriptShader006 : MonoBehaviour
{

	private Material mat;
	private float value;
	float speed = 2.5f;

	void Start ()
	{
		mat = GetComponent<MeshRenderer> ().sharedMaterial;
	}

	void Update ()
	{
		value = Mathf.PingPong (Time.time * speed, 5);
		mat.SetFloat ("_Strength", value);
		Debug.Log (value);
	}
}
