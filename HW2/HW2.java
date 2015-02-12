
import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HW2 {
	static Connection conn = null;
	static Statement stmt = null;
	
	public static void main(String[] args) throws IOException, SQLException {
		try {
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:wyn","scott","tiger");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(args[0].equals("window")) {
			window(args);
		}
		if(args[0].equals("within")) {
			within(args);
		}
		if(args[0].equals("nn")) {
			nn(args);
		}
		if(args[0].equals("demo")) {
			demo(args);
		}
	}
	
	private static void window(String[] args) throws SQLException {
		try {
			Statement stmt1 = conn.createStatement();	
			String query = query_generator(args);
			System.out.println(query);
			ResultSet rs1 = (stmt1).executeQuery(query);
			int count = 0;
			while (rs1.next()) {
				count++;
				String id = rs1.getString("BID");
				System.out.println(id);
			}
			System.out.println("Total number: " + count);
			stmt1.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static String query_generator(String[] args) {
		String query = null;
		if(args[1].equals("building")) {
			query = "SELECT B.BID FROM BUILDING B WHERE SDO_INSIDE(B.SHAPE,";
		}
		else if(args[1].equals("firebuilding")) {
			query = "SELECT F.BID FROM FIREBUILDING F WHERE SDO_INSIDE(F.SHAPE,";
		}
		else if(args[1].equals("firehydrant")) {
			query = "SELECT H.BID FROM HYDRANT H WHERE SDO_INSIDE(H.SHAPE,";
		}
		query += "SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1,1003,3),";
		query += "SDO_ORDINATE_ARRAY(" + args[2] + "," + args[3] + "," + args[4] + "," + args[5];
		query += "))) = 'TRUE' ORDER BY BID";
		
		return query;		
	}
	
    private static void within(String[] args) throws SQLException {
    	try {
			Statement stmt2 = conn.createStatement();	
			String query = query_generator2(args);
			System.out.println(query);
			ResultSet rs1 = (stmt2).executeQuery(query);
			int count = 0;
			while (rs1.next()) {
				count++;
				String id = rs1.getString("BID");
				System.out.println(id);
			}
			System.out.println("Total number: " + count);
			stmt2.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    private static String query_generator2(String[] args) {
		String query = null;
		if(args[1].equals("building")) {
			query = "SELECT B.BID FROM BUILDING B, BUILDING B1";
		}
		else if(args[1].equals("firebuilding")) {
			query = "SELECT B.BID FROM FIREBUILDING B, BUILDING B1";
		}
		else if(args[1].equals("firehydrant")) {
			query = "SELECT B.BID FROM HYDRANT B, BUILDING B1";
		}
		query += " WHERE B1.BNAME = '" + args[2] + "'" + "AND SDO_WITHIN_DISTANCE(B.SHAPE," +
				" B1.SHAPE, 'DISTANCE = " + args[3] + "') = 'TRUE' AND B.BID <> B1.BID";
		
		return query;		
	}
    
    private static void nn(String[] args) {
    	try {
			Statement stmt3 = conn.createStatement();	
			String query = query_generator3(args);
			System.out.println(query);
			ResultSet rs1 = (stmt3).executeQuery(query);
			int count = 0;
			while (rs1.next()) {
				count++;
				String id = rs1.getString("BID");
				System.out.println(id);
			}
			System.out.println("Total number: " + count);
			stmt3.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
    private static String query_generator3(String[] args) {
		String query = null;
		if(args[1].equals("building")) {
			query = "SELECT B.BID FROM BUILDING B, BUILDING B1";
		}
		else if(args[1].equals("firebuilding")) {
			query = "SELECT B.BID FROM FIREBUILDING B, BUILDING B1";
		}
		else if(args[1].equals("firehydrant")) {
			query = "SELECT B.BID FROM HYDRANT B, BUILDING B1";
		}
		if(args[1].equals("building")) {
			query += " WHERE B1.BID = '" + args[2] + "'" + " AND SDO_NN(B.SHAPE," +
				" B1.SHAPE, 'SDO_NUM_RES = " + (Integer.parseInt(args[3]) + 1)
				+ "') = 'TRUE' AND B.BID <> B1.BID  AND ROWNUM <= " + args[3];
		}
		else {
			query += " WHERE B1.BID = '" + args[2] + "'" + " AND SDO_NN(B.SHAPE," +
					" B1.SHAPE, 'SDO_NUM_RES = " + args[3] + "') = 'TRUE' AND B.BID <> B1.BID";
		}
		
		return query;		
	}
    
    private static void demo(String[] args) {
		if(args[1].equals("1")) {
			demo1();
		}
		
		if(args[1].equals("2")) {
			demo2();
		}
		
		if(args[1].equals("3")) {
			demo3();
		}
		
		if(args[1].equals("4")) {
			demo4();
		}
		
		if(args[1].equals("5")) {
			demo5();
		}
	}
    
    private static void demo1() {
    	try {
			Statement stmt11 = conn.createStatement();	
			String query = "SELECT DISTINCT B.BNAME FROM BUILDING B, FIREBUILDING F WHERE B.BID = F.BID" +
					" OR SDO_WITHIN_DISTANCE(B.SHAPE,F.SHAPE,'DISTANCE = 100') = 'TRUE' ORDER BY B.BNAME";
			System.out.println(query);
			ResultSet rs1 = (stmt11).executeQuery(query);
			int count = 0;
			while (rs1.next()) {
				count++;
				String id = rs1.getString("BNAME");
				System.out.println(id);
			}
			System.out.println("Total number: " + count);
			stmt11.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private static void demo2() {
    	try {
			Statement stmt12 = conn.createStatement();	
			String query = "SELECT DISTINCT H.BID FROM HYDRANT H, FIREBUILDING F WHERE" +
					" SDO_NN(H.SHAPE,F.SHAPE,'SDO_NUM_RES = 5') = 'TRUE'";
			System.out.println(query);
			ResultSet rs1 = (stmt12).executeQuery(query);
			int count = 0;
			while (rs1.next()) {
				count++;
				String id = rs1.getString("BID");
				System.out.println(id);
			}
			System.out.println("Total number: " + count);
			stmt12.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private static void demo3() {
    	try {
			Statement stmt13 = conn.createStatement();	
			String query = "SELECT F.FNAME, H.BID, B.BNAME FROM FIREBUILDING F, " +
					"HYDRANT H, BUILDING B WHERE SDO_NN(H.SHAPE, F.SHAPE, 'SDO_NUM_RES = 1') = 'TRUE' " +
					"INTERSECT SELECT F.FNAME, H.BID, B.BNAME FROM FIREBUILDING F, HYDRANT H, BUILDING B " +
					"WHERE SDO_WITHIN_DISTANCE(B.SHAPE,H.SHAPE,'DISTANCE = 100') = 'TRUE'";
			System.out.println(query);
			ResultSet rs1 = (stmt13).executeQuery(query);
			int count = 0;
			while (rs1.next()) {
				count++;
				String fname = rs1.getString("FNAME");
				System.out.print(fname + "   ");
				String id = rs1.getString("BID");
				System.out.print(id + "   ");
				String bname = rs1.getString("BNAME");
				System.out.println(bname);
			}
			System.out.println("Total number: " + count);
			stmt13.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private static void demo4() {
    	try {
    		int test = 0;
			Statement stmt14 = conn.createStatement();	
			String query = "SELECT H.BID, B.BNAME FROM BUILDING B, HYDRANT H WHERE " +
					"SDO_NN(H.SHAPE,B.SHAPE,'SDO_NUM_RES = 1') = 'TRUE'";
			System.out.println(query);
			ResultSet rs1 = (stmt14).executeQuery(query);
			while (rs1.next()) {
				String id = rs1.getString("BID");
				if(id.equals("p30")) {
					test = 1;
					String name = rs1.getString("BNAME");
					System.out.println(name);
				}
			}
			if(test == 0) {
				System.out.println("No building is found");
			}
			stmt14.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private static void demo5() {
    	try {
			Statement stmt12 = conn.createStatement();	
			String query = "SELECT H.BID,NVL(TEMP.NUM,0) AS NUM_BUILDING FROM HYDRANT H LEFT JOIN" +
					"(SELECT H.BID AS BID, COUNT(*) AS NUM FROM BUILDING B, HYDRANT H " +
					"WHERE SDO_NN(H.SHAPE, B.SHAPE, 'SDO_NUM_RES = 1') = 'TRUE' GROUP BY H.BID" +
					") TEMP ON H.BID = TEMP.BID ORDER BY H.BID" ;
			System.out.println(query);
			ResultSet rs1 = (stmt12).executeQuery(query);
			System.out.println("ID" + "     num_building");
			while (rs1.next()) {			
				String id = rs1.getString("BID");
				System.out.print(id + "        ");
				String num = rs1.getString("NUM_BUILDING");
				System.out.println(num);
			}
			stmt12.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

}
