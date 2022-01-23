package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDetailsDAO 
{
	Connection con;
	Statement stmt;
	ResultSet rs;
	public UserDetailsDAO(String driver,String url)
	{
		//System.out.println("Userdao called");
		//System.out.println("Url is:"+url);
		//System.out.println(driver);
		try
		{
			Class.forName(driver);
			//System.out.println("Driver is :"+driver);
			con = DriverManager.getConnection(url,"root","root");
			
			//System.out.println("Connection created!!");
			//jdbc:mysql://127.0.0.1:3306/?user=root
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public boolean insert_user(String fname,String email,String mobileno,String username,String password)
	{
		boolean flag = true;
		try 
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select email,username from user_details");
			while(rs.next())
			{
				if(rs.getString(1).equalsIgnoreCase(email)||rs.getString(2).equals(username))
				{
					flag = false;
					break;
				}
			}
			if(flag)
			{
				int i = stmt.executeUpdate("insert into user_details(full_name,email,mobileno,username,password) VALUES('"+fname+"','"+email+"','"+mobileno+"','"+username+"','"+password+"')");
				if(i==1)
					return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	public User check_user(String username,String password) 
	{
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select user_id,full_name,email,mobileno,username,password from user_details");
			while(rs.next())
			{
				if(rs.getString(5).equalsIgnoreCase(username)&&rs.getString(6).equals(password))
				{
					int id = rs.getInt(1);
					String fname = rs.getString(2);
					String email = rs.getString(3);
					String mobile = rs.getString(4);
					String user = rs.getString(5);
					String pass = rs.getString(6);
					return new User(id,fname,email,mobile,user,pass);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
	public int check_user_details(int userid,String fname,String email,String mobileno,String username)
	{
		int flag = 0;
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from user_details");
			while(rs.next())
			{
				if(rs.getString(3).equalsIgnoreCase(email)&&rs.getString(4).equals(mobileno)&&rs.getString(5).equalsIgnoreCase(username))
				{
					if(rs.getString(2).equalsIgnoreCase(fname))
					{
						flag = 0;
						break;
					}
					else
					{
						flag = 1;
						break;
					}
				}
				else
				{
					flag = 2;
				}
			}
			if(flag==2)
			{
				while(rs.next())
				{
					if(rs.getInt(1)!=userid)
					{
						if(rs.getString(3).equalsIgnoreCase(email)||rs.getString(4).equals(mobileno)||rs.getString(4).equalsIgnoreCase(username))
						{
							flag = -1;
							break;
						}
					}
				}
				flag = 1;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return flag;
	}
	public boolean user_details_update(int userid,String fname,String email,String mobileno,String username)
	{
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("update user_details set full_name='"+fname+"',email='"+email+"',mobileno='"+mobileno+"',username='"+username+"' where user_id="+userid+";");
			return true;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	public boolean change_user_password(int id,String oldpass,String newpass)
	{
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select password from user_details where user_id="+id);
			rs.next();
			if(rs.getString(1).equals(oldpass))
			{
				stmt.executeUpdate("update user_details set password='"+newpass+"' where user_id="+id);
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	public void delete_user(int id)
	{
		try
		{
			stmt = con.createStatement();
			stmt.executeUpdate("delete from user_details where user_id="+id);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	public boolean insert_operator(String operator,String company)
	{
		boolean flag = true;
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select operator_name from operator_details");
			while(rs.next())
			{
				if(rs.getString(1).equalsIgnoreCase(operator))
				{
					flag = false;
					break;
				}
			}
			if(flag)
			{
				Integer operatorID = null;
				rs = stmt.executeQuery("select * from operator_details order by operator_id desc");
				while(rs.next()){
					operatorID = rs.getInt(1);
					break;
				}
				if(operatorID != null) {
					operatorID++;
				}else {
					operatorID = 1;
				}
				System.out.println("operatorID is : "+ operatorID );
				int i=stmt.executeUpdate("insert into operator_details (operator_id, operator_name,company_name) values("+ operatorID + ",'"+ operator+"','"+company+"')");
				if(i==1)
					return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	public boolean insert_mobileno(String mobileno,String operatorid,String owner)
	{
		boolean flag = true;
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select mobile_no,operator_id from mobileno_details");
			while(rs.next())
			{
				if(rs.getString(1).equalsIgnoreCase(mobileno))
				{
					flag = false;
					break;
				}
			}
			if(flag)
			{
				int i=stmt.executeUpdate("insert into mobileno_details (mobile_no,operator_id,owner_name) values('"+mobileno+"',"+operatorid+",'"+owner+"')");
				if(i==1)
					return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	public List<Operator> operator_list()
	{
		List<Operator> oplist = new ArrayList<>();
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from operator_details");
			while(rs.next())
			{
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String company = rs.getString(3);
				oplist.add(new Operator(id,name,company));	
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return oplist;
	}
	public boolean check_plan_exist(String operatorid,String price)
	{
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select plan_id from recharge_plan_details where operator_id="+operatorid+" and price="+price);
			if(rs.next()==false)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return true;
	}
	public boolean add_plan(String operatorid,String plandetails,String planvalidity,String price)
	{
		try
		{
			stmt = con.createStatement();
			Integer planID = null;
			rs = stmt.executeQuery("select * from recharge_plan_details order by plan_id desc");
			while(rs.next()) {
				planID = rs.getInt(1);
				break;
			}
			if(planID != null) {
				planID++;
			}else {
				planID = 1;
			}
			int i = stmt.executeUpdate("insert into recharge_plan_details(plan_id,operator_id,plan_details,plan_validity,price) values("+ planID + "," + operatorid+",'"+plandetails+"','"+planvalidity+"',"+price+")");
			if(i==1)
			{
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	public List<Plan> plan_list(int operator_id)
	{
		List<Plan> planlist = new ArrayList<>();
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from recharge_plan_details where operator_id="+operator_id);
			while(rs.next())
			{
				int planid = rs.getInt(1);
				int operatorid = rs.getInt(2);
				String plandetails = rs.getString(3);
				String planvalidity = rs.getString(4);
				float price = rs.getFloat(5);
				planlist.add(new Plan(planid, operatorid, plandetails, planvalidity, price));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return planlist;
	}
	public boolean mobile_recharge(String mobileno,String operatorid,String planid,String amount)
	{
		try
		{
			stmt = con.createStatement();
			rs = stmt.executeQuery("select mobile_no,operator_id from mobileno_details");
			System.out.println("Mobile Number :"+ mobileno);
			System.out.println("operatorid : "+operatorid);
			while(rs.next())
			{
				if(rs.getString(1).equals(mobileno)&&rs.getInt(2)==Integer.parseInt(operatorid))
				{
					Integer lastTransactionID = null;
					rs = stmt.executeQuery("select * from mobile_recharge order by transaction_id desc");
					while(rs.next()) {
						lastTransactionID = rs.getInt(1);
						break;
					}
					if(lastTransactionID != null) {
						lastTransactionID++;
					}else {
						lastTransactionID = 1;
					}
					System.out.println("lastTransactionID is : " + lastTransactionID);
					stmt.executeUpdate("insert into mobile_recharge(transaction_id, operator_id,mobile_no,plan_id,amount) values("+lastTransactionID + "," + operatorid + ",'" + mobileno + "'," + (planid.isEmpty()?null:planid) + ","+ amount + ")");
					return true;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
}