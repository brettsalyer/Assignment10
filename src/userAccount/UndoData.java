package userAccount;


import java.io.Serializable;

public class UndoData implements Serializable{
    private int actionCode;
    private Object actionValue;
    private FacebookUser user;
    
    public UndoData(int a, Object v, FacebookUser u){
        actionCode = a;
        actionValue = v;
        user = u;
    }
    
    public int getActionCode(){
        return actionCode;
    }
    
    public Object getActionValue(){
        return actionValue;
    }
    
    public FacebookUser getUser(){
        return user;
    }
}
