using UnityEngine;
using System.Collections;

public class MovementController : MonoBehaviour 
{
	public float maxSpeed = 10f;
	public float jumpForce = 600f;
	bool facingRight = true;

	Animator anim;

	bool isGrounded = false;
	public Transform groundCheck;
	float groundRadius = 0.2f;
	public LayerMask whatIsGround;

	bool doubleJump = false;

	// Use this for initialization
	void Start () 
	{
		anim = GetComponent<Animator>();
	}
	
	// Update is called once per frame
	void FixedUpdate () 
	{
		isGrounded = Physics2D.OverlapCircle (groundCheck.position, groundRadius, whatIsGround);
		anim.SetBool ("Ground", isGrounded);

		if(isGrounded)
			doubleJump = false;

		//anim.SetFloat ("VerticalSpeed", rigidbody2D.velocity.y);

		if(!isGrounded) return;

		float move = Input.GetAxis("Horizontal");
		anim.SetFloat("Speed", Mathf.Abs (move));
		GetComponent<Rigidbody2D>().velocity = new Vector2(move * maxSpeed, GetComponent<Rigidbody2D>().velocity.y);

		if(move > 0 && !facingRight)
		{
			FlipFacing();
		}
		else if(move < 0 && facingRight)
		{
			FlipFacing();
		}
	}

	void Update()
	{
		if((isGrounded || !doubleJump) && Input.GetKeyDown (KeyCode.Space))
		{
			anim.SetBool ("Ground", false);
			GetComponent<Rigidbody2D>().AddForce (new Vector2(0, jumpForce));

			if(!doubleJump && !isGrounded)
				doubleJump = true;
		}
	}

	void FlipFacing()
	{
		facingRight = !facingRight;
		Vector3 charScale = transform.localScale;
		charScale.x *= -1;
		transform.localScale = charScale;
	}
}
