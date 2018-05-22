package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.ArtObjectAndCount;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects ";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
   public List<ArtObject> listaPerArchi(ArtObject ao1, ArtObject ao2) {
		
		String sql = "SELECT eo1.*, eo2.* "
				+ "FROM exhibition_objects eo1, exhibition_objects eo2 "
				+ "WHERE eo1.exhibition_id = eo2.exhibition_id "
				+ "AND eo1.object_id = ? "
				+ "AND eo1.object_id = ? ";
		
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

   public int contaExhibitionComuni(ArtObject partenza, ArtObject arrivo) {
	

	String sql = "SELECT count(*) AS cnt "
			+ "FROM exhibition_objects eo1, exhibition_objects eo2 "
			+ "WHERE eo1.exhibition_id = eo2.exhibition_id "
			+ "AND eo1.object_id = ? "
			+ "AND eo2.object_id = ? ";
	
	
	Connection conn = DBConnect.getConnection();
	int conteggio = 0;

	try {
		PreparedStatement st = conn.prepareStatement(sql);
		
		st.setInt(1, partenza.getId());
		st.setInt(2, arrivo.getId());
		
		ResultSet res = st.executeQuery();
		
		res.next();
		
		conteggio = res.getInt("cnt");
		
		conn.close();
		
		
	} catch (SQLException e) {
		e.printStackTrace();
		throw new RuntimeException(e);
	}
	
	return conteggio;
	
}
   
   public List<ArtObjectAndCount> listArtObjectAndCount(ArtObject ao) {
	  String sql = "SELECT count(eo2.exhibition_id) as cnt , eo2.object_id as id " + 
	  		"FROM exhibition_objects eo1, exhibition_objects eo2 " + 
	  		"WHERE eo1.exhibition_id = eo2.exhibition_id " + 
	  		"AND eo1.object_id = ? " + 
	  		"AND eo2.object_id > eo1.object_id " + 
	  		"GROUP BY eo2.object_id ";
	  
	  Connection conn = DBConnect.getConnection();
	  
	  List<ArtObjectAndCount> result = new ArrayList<>();
	  
	  try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, ao.getId());
			
			
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				result.add(new ArtObjectAndCount( res.getInt("id"), res.getInt("cnt")));
			}
			
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return result;
	  
	  
	   
   }
}
