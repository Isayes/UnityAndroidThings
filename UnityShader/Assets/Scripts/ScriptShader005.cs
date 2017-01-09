using UnityEngine;
using System.Collections;

[ExecuteInEditMode]
public class ScriptShader005 : MonoBehaviour
{
	// 绑定材质
	public Material mat;

	// 存储获取的 3D 坐标
	Vector3[] worldPos;

	// 存储待绘制的多边形顶点屏幕坐标
	Vector4[] screenPos;

	// 多边形顶点总数
	int maxPointNum = 6;

	// 当前已经获得的顶点数
	int currentPointNum = 0;

	// 传递顶点数量给 Shader
	int pointNum2Shader = 0;

	// 是否处于顶点获取过程
	bool InSelection = true;

	void Start ()
	{
		worldPos = new Vector3[maxPointNum];
		screenPos = new Vector4[maxPointNum];
	}

	void Update ()
	{
		// 传递顶点屏幕位置信息给 shader
		mat.SetVectorArray ("Value", screenPos);

		// 传递顶点数量给 shader
		mat.SetInt ("PointNum", pointNum2Shader);

		// 使用摄像机发射一条射线 , 以获取要选择的 3D 位置
		Ray ray = Camera.main.ScreenPointToRay (Input.mousePosition);
		RaycastHit hit;
		if (Physics.Raycast (ray, out hit, 100)) {
			Debug.DrawLine (ray.origin, hit.point, Color.red);
		}

		// 利用鼠标点击来获取位置信息
		if (Input.GetMouseButtonDown (0) && InSelection) {
			if (currentPointNum < maxPointNum) {
				currentPointNum++;
				pointNum2Shader++;
				worldPos [currentPointNum - 1] = hit.point;
				Vector3 v3 = Camera.main.WorldToScreenPoint (worldPos [currentPointNum - 1]);
				screenPos [currentPointNum - 1] = new Vector4 (v3.x, v3.y, v3.z, 0);
			} else {
				// 超过了多边形顶点总数就不能继续获取
				InSelection = false;
			}
		}

		// 实时更新已选择的 3D 点的屏幕位置
		for (int i = 0; i < maxPointNum; i++) {
			Vector3 v3 = Camera.main.WorldToScreenPoint (worldPos [i]);
			screenPos [i] = new Vector4 (v3.x, v3.y, v3.z, 0);
		}

		// 检测是否有 3D 点移动到了摄像机后面 , 如果有 , 则停止绘制
		for (int i = 0; i < currentPointNum; i++) {
			if (Vector3.Dot (worldPos [i] - Camera.main.transform.position, Camera.main.transform.forward) <= 0) {
				pointNum2Shader = 0;
				break;
			}
			pointNum2Shader = currentPointNum;
		}

	}

	// 抓取当前的渲染图像进行处理
	void OnRenderImage (RenderTexture src, RenderTexture dest)
	{
		Graphics.Blit (src, dest, mat);
	}
}
