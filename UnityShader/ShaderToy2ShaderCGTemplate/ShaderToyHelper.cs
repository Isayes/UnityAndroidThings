using UnityEngine;
using System.Collections;

/// <summary>
/// 把该脚本拖拽到材质所在的物体上，同时保证该物体上绑定了 Collider 
/// </summary>
public class ShaderToyHelper : MonoBehaviour
{
    private Material _material = null;
    private bool _isDragging = false;

    void Start()
    {
        Renderer render = GetComponent<Renderer>();
        if (render != null)
            _material = render.material;
        _isDragging = false;
    }

    void Update()
    {
        Vector3 mousePosition = Vector3.zero;

        // 在有鼠标拖拽时，mousePosition 的 Z 分量为 1， 否则为 0，与 ShaderToy 中判断鼠标的方式一致
        if (_isDragging)
            mousePosition = new Vector3(Input.mousePosition.x, Input.mousePosition.y, 1.0f);
        else
            mousePosition = new Vector3(Input.mousePosition.x, Input.mousePosition.y, 0.0f);

        if (_material != null)
            _material.SetVector("iMouse", mousePosition);
    }

    void OnMouseDown() {
        _isDragging = true;
    }

    void OnMouseUp() {
        _isDragging = false;
    }


}