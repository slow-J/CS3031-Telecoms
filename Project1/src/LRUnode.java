public class LRUnode 
{
  String key;
  int value;
  LRUnode prev;
  LRUnode next;

  public LRUnode(String key, int value) 
  {
    this.key = key;
    this.value = value;
  }
}