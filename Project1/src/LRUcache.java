import java.util.ArrayList;

public class LRUcache
{
  ArrayList<LRUnode> list = null;
  int capacity;
 
  public LRUcache(int capacity) 
  {
    this.list = new ArrayList<>();
    this.capacity = capacity;
  }
  public boolean checkIfInCache(String cmpkey)
  {
    //doesnt make head as not accessed yet
    for(int i=0;i<list.size();i++)
    {
      if(list.get(i).key.equals(cmpkey))
      {
        return true;
      }
    }
    return false;
  }
  //for testing purposes only
  public void getList()
  {
    for(int i=0; i<4;i++)
    {
      System.out.println(list.get(i).key+" "+list.get(i).value);
    }
  }
  public int getResponse(String key)
  {
    for(int i=0;i<list.size();i++)
    {
      if(list.get(i).key.equals(key))
      {
        // store as temporary node before removing and adding as new head
        LRUnode tmp = list.get(i);
        list.remove(list.get(i));
        //make head
        list.add(0, tmp);
        return tmp.value;
      }
    }
    //shouldnt do this as checkInCache should be called before getResponse
    return Integer.MAX_VALUE;
  }
  public void addLRUnode(String cmpkey, int val)
  {
    boolean trigger = false;
    for(int i=0;i<list.size();i++)
    {
      if(list.get(i).key.equals(cmpkey))
      {
        trigger=true;
        LRUnode tmp = list.get(i);
        list.remove(list.get(i));
        //make head
        list.add(0, tmp);
      }
    }
    if(!trigger)
    {
      if(list.size()>=this.capacity)
      {
        list.remove(this.capacity-1);
      }
      LRUnode tmp = new LRUnode(cmpkey, val);
      //make head
      list.add(0, tmp);
    }
    
  }
 
}