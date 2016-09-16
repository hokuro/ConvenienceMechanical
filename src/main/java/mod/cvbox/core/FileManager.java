package mod.cvbox.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FileManager {
	public FileManager(){
	}

	public static void read_file(String user_name){
		String parent = "/saves/"+FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName() + "/ExpBankSave";
		String chiled = "/" + user_name + ".dat";
		String path = "";
		for (int n = 0; n < PlayerExpBank.player_name.size(); n++){
			if(((String)PlayerExpBank.player_name.get(n)).equals(user_name)){
				PlayerExpBank.player_name.remove(n);
				PlayerExpBank.player_exp.remove(n);
				PlayerExpBank.box_exp.remove(n);
				break;
			}
		}

		if (FMLCommonHandler.instance().getSide().isClient()){
			path = FMLClientHandler.instance().getClient().mcDataDir + parent;
		}else if (FMLCommonHandler.instance().getSide().isServer()){
			path = "./config/ExpBox/saves/";
		}
		File folder = new File(path);
		if(!folder.exists()){
			folder.mkdirs();
		}
		path = path + chiled;
		File file = new File(path);
		if (!file.exists()){
			try{
				FileWriter fw = new FileWriter(path, false);
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

				pw.print("player");
				pw.print(",");
				pw.print("exp");
				pw.println();
				pw.close();
			}catch(IOException ex){

			}
		}
		try{
			File csv = new File(path);
			BufferedReader br = new BufferedReader(new FileReader(csv));

			String line = "";
			br.readLine();
			while((line = br.readLine())!=null){
				StringTokenizer token = new StringTokenizer(line,",");
					while(token.hasMoreTokens()){
						String name = token.nextToken();
						PlayerExpBank.player_name.add(name);
						int val = Integer.parseInt(token.nextToken());
						PlayerExpBank.box_exp.add(Integer.valueOf(val));
						PlayerExpBank.player_exp.add(Integer.valueOf(0));
					}
			}
			br.close();
		}catch(FileNotFoundException e){
		}catch(IOException e){
		}
	}

	public static void save_file(String user_name) {
		String parent = "/saves/" + FMLCommonHandler.instance().getMinecraftServerInstance().getFolderName()
				+ "/ExpBankSave";
		String chiled = "/" + user_name + ".dat";
		String path = "";
		if (FMLCommonHandler.instance().getSide().isClient()) {
			path = FMLClientHandler.instance().getClient().mcDataDir + parent + chiled;
		} else if (FMLCommonHandler.instance().getSide().isServer()) {
			path = "./config/ExPCotainerMod/saves" + chiled;
		}
		try {
			FileWriter fw = new FileWriter(path, false);

			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

			pw.print("player");
			pw.print(",");
			pw.print("exp");
			pw.println();
			for (int n = 0; n < PlayerExpBank.player_name.size(); n++) {
				if (((String) PlayerExpBank.player_name.get(n)).equals(user_name)) {
					pw.print((String) PlayerExpBank.player_name.get(n));
					pw.print(",");
					pw.print(PlayerExpBank.box_exp.get(n));
					pw.println();
					break;
				}
			}
			pw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}}
