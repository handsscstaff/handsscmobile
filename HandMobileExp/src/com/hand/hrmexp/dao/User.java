 /**
 * 
 */
package com.hand.hrmexp.dao;

import com.littlemvc.db.annotation.Id;
import com.littlemvc.db.annotation.Table;

/**
 * @author jiang titeng
 *
 * All right reserve
 */
@Table(name="User_Table")
public class User {
	
	@Id(column="id")
	public int id;
	public int age;
	public int height;
	public int weight;
	public String name; 


	public User() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	public void setAge(int age){
		
         this.age = age;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	


}
