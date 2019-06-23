package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoodDao {
	

	public List<Food> listAllFood(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiment(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Condiment c= new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							);
					
					list.add(c);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	public List<Condiment> ingredientiCalorieInferiori(double calorie, HashMap<Integer, Condiment> map) {
		String sql = "SELECT condiment_id, food_code, display_name, condiment_portion_size, condiment_calories " + 
				"FROM condiment " + 
				"WHERE condiment_calories<? " + 
				"ORDER BY condiment_calories desc" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDouble(1, calorie);
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Condiment c= new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							);
					map.put(res.getInt("food_code"),c );
					list.add(c);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		}
	}
	public List<IngredientiComuni> ingredientiComuni(double calorie, HashMap<Integer, Condiment> map ) {
		String sql = "SELECT f1.condiment_food_code AS i1code, f2.condiment_food_code AS i2code, COUNT(DISTINCT f1.food_code) AS cnt " + 
				"FROM food_condiment f1, food_condiment f2, condiment c1, condiment c2 " + 
				"WHERE f1.food_code= f2.food_code AND f1.condiment_food_code>f2.condiment_food_code " + 
				"AND f1.condiment_food_code=c1.food_code AND c1.condiment_calories<? " + 
				"AND f2.condiment_food_code=c2.food_code AND c2.condiment_calories<? " + 
				"GROUP BY f1.condiment_food_code, f2.condiment_food_code ";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setDouble(1, calorie);
			st.setDouble(2, calorie);
			
			List<IngredientiComuni> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					IngredientiComuni i= new IngredientiComuni(map.get(res.getInt("i1code")), map.get(res.getInt("i2code")), res.getInt("cnt"));
					list.add(i);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
			
		}
	}
}
