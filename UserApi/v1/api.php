<?php 
 
	//getting the dboperation class
	require_once '../includes/CRUD_BD.php';
 
	//function validating all the paramters are available
	//we will pass the required parameters to this function 
	function parametros_validos($parametros){
		//assuming all parameters are available 
		$disponible = true; 
		$parametros_faltantes = ""; 
		
		foreach($parametros as $parametro){
			if(!isset($_POST[$parametro]) || strlen($_POST[$parametro])<=0){
				$disponible = false; 
				$parametros_faltantes = $parametros_faltantes . ", " . $parametro; 
			}
		}
		
		//if parameters are missing 
		if(!$disponible){
			$response = array(); 
			$response['error'] = true; 
			$response['message'] = 'Los siguientes parametros no estan disponibles ' . substr($parametros_faltantes, 1, strlen($parametros_faltantes)) . '.';
			
			//displaying error
			echo json_encode($response);
			
			//stopping further execution
			die();
		}
	}
	
	//an array to display response
	$response = array();
	
	//if it is an api call 
	//that means a get parameter named api call is set in the URL 
	//and with this parameter we are concluding that it is an api call
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			//the CREATE operation
			//if the api call value is 'createhero'
			//we will create a record in the database
			case 'createuser':
				//first check the parameters required for this request are available or not 
				parametros_validos(array('name','phone','country','age'));
				
				//creating a new dboperation object
				$db = new CRUD_BD();
				
				//creating a new record in the database
				$result = $db->createUser(
					$_POST['name'],
					$_POST['phone'],
					$_POST['country'],
					$_POST['age']
				);
				
 
				//if the record is created adding success to response
				if($result){
					//record is created means there is no error
					$response['error'] = false; 
 
					//in message we have a success message
					$response['message'] = 'Se ha creado el usuario correctamente';
 
					//and we are getting all the heroes from the database in the response
					$response['users'] = $db->getUsers();
				}else{
 
					//if record is not added that means there is an error 
					$response['error'] = true; 
 
					//and we have the error message
					$response['message'] = 'Hubo un error creando el usuario.';
				}
				
			break; 
			
			//the READ operation
			//if the call is getheroes
			case 'getusers':
				$db = new CRUD_BD();
				$response['error'] = false; 
				$response['message'] = 'Request completada.';
				$response['users'] = $db->getUsers();
			break; 
			
			
			//the UPDATE operation
			case 'updateuser':
				parametros_validos(array('iduser','name','phone','country','age'));
				$db = new CRUD_BD();
				$result = $db->updateUser(
					$_POST['iduser'],
					$_POST['name'],
					$_POST['phone'],
					$_POST['country'],
					$_POST['age']
				);
				
				if($result){
					$response['error'] = false; 
					$response['message'] = 'Se ha actualizado el usuario.';
					$response['users'] = $db->getUsers();
				}else{
					$response['error'] = true; 
					$response['message'] = 'Ocurrio un error actualizando el usuario';
				}
			break; 
			
			//the delete operation
			case 'deleteuser':
 
				//for the delete operation we are getting a GET parameter from the url having the id of the record to be deleted
				if(isset($_GET['iduser'])){
					$db = new CRUD_BD();
					if($db->deleteUser($_GET['iduser'])){
						$response['error'] = false; 
						$response['message'] = 'Se ha eliminado el usuario';
						$response['heroes'] = $db->getUsers();
					}else{
						$response['error'] = true; 
						$response['message'] = 'Ocurrio un error borrando un usuario';
					}
				}else{
					$response['error'] = true; 
					$response['message'] = 'Nothing to delete, provide an id please';
				}
			break; 
		}
		
	}else{
		//if it is not api call 
		//pushing appropriate values to response array 
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	//displaying the response in json structure 
	echo json_encode($response);