package MMC;

public class Encryption {

	public Encryption() {
		// TODO Auto-generated constructor stub
	}

	public static String encrypt(String name)
	{
		char[] ar=name.toCharArray();
		for(int i=0;i<ar.length;i++)
		{
			int n=ar[i];
			n=n+7;
			ar[i]=(char) n;
		}
		return String.valueOf(ar);
	}
	public static String decrypt(String name)
	{
		char[] ar=name.toCharArray();
		for(int i=0;i<ar.length;i++)
		{
			int n=ar[i];
			n=n-7;
			ar[i]=(char) n;
		}
		return String.valueOf(ar);
	}
}
