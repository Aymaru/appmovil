<?php
 
class CRUD_BD
{
    //Database connection link
    private $con;
 
    //Class constructor
    function __construct()
    {
        //Getting the DbConnect.php file
        require_once dirname(__FILE__) . '/Conectar_BD.php';
 
        //Creating a DbConnect object to connect to the database
        $db = new Conectar_BD();
 
        //Initializing our connection link of this class
        //by calling the method connect of DbConnect class
        $this->conn = $db->connect();
    }
	
	/*
	* The create operation
	* When this method is called a new record is created in the database
	*/
	function createUser($name, $phone, $country, $age){
		$query = $this->conn->prepare("INSERT INTO user (name, phone, country, age) VALUES (?, ?, ?, ?)");
		$query->bind_param("sisi", $name, $phone, $country, $age);
		if($query->execute())
			return true; 
		return false; 
	}
 
	/*
	* The read operation
	* When this method is called it is returning all the existing record of the database
	*/
	function getUsers(){
		$query = $this->conn->prepare("SELECT iduser, name, phone, country, age FROM user");
		$query->execute();
		$query->bind_result($iduser, $name, $phone, $country, $age);
		
		$users = array(); 
		
		while($query->fetch()){
			$user  = array();
			$user['id'] = $iduser; 
			$user['name'] = $name; 
			$user['phone'] = $phone; 
			$user['country'] = $country; 
			$user['age'] = $age; 
			
			array_push($users, $user); 
		}
		
		return $users; 
	}
	
	/*
	* The update operation
	* When this method is called the record with the given id is updated with the new given values
	*/
	function updateUser($iduser, $name, $phone, $country, $age){
		$query = $this->conn->prepare("UPDATE user SET name = ?, phone = ?, country = ?, age = ? WHERE iduser = ?");
		$query->bind_param("sisii", $name, $phone, $country, $age, $iduser);
		if($query->execute())
			return true; 
		return false; 
	}
	
	
	/*
	* The delete operation
	* When this method is called record is deleted for the given id 
	*/
	function deleteUser($iduser){
		$query = $this->conn->prepare("DELETE FROM user WHERE iduser = ? ");
		$query->bind_param("i", $iduser);
		if($query->execute())
			return true; 
		
		return false; 
	}
}