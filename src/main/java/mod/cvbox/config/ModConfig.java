package mod.cvbox.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.math.NumberUtils;

import mod.cvbox.core.ModCommon;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfig {
	public static final ModConfig config = new ModConfig();
	protected Class<?>[] _cls = null;
	protected File _file = null;
	protected long _lastModify = 0L;
	protected long _checkTime = 0L;

	public File ConfigFile(){return _file;}
	public void init(Class<?>[] cls, FMLPreInitializationEvent event){
		_cls = cls;
		_file = event.getSuggestedConfigurationFile();
		loadConfig();
	}

	public boolean reloadConfig(){
		long n = System.currentTimeMillis() - _checkTime;
		if (n < ModCommon.MOD_CONFIG_RELOAD){
			return false;
		}
		if (!_file.isFile()){
			return false;
		}
		if (_lastModify == _file.lastModified()){
			return false;
		}
		loadConfig();
		_checkTime = System.currentTimeMillis();
		return true;
	}

	protected void loadConfig(){
		boolean isSave = (!_file.isFile()) || (_file.length() <= 0L);
		Configuration config = new Configuration(_file);
		config.load();
		for (Class<?> cls : _cls){
			Field[] fields = cls.getFields();
			for (Field fld : fields){
				ConfigProperty prop = (ConfigProperty)fld.getAnnotation(ConfigProperty.class);
				if ( prop == null){continue;}

				if (Modifier.isStatic(fld.getModifiers())){
					Class<?> type = fld.getType();
					Property p = null;
					try {
						if (!config.hasCategory(prop.category())){
							isSave = true;
						}
						String comment=prop.comment();
						if (Integer.TYPE.equals(type)){
							p = config.get(prop.category(), fld.getName(), fld.getInt(null));
							fld.setInt(null, p.getInt());
							if (NumberUtils.isDigits(prop.max())){
								int max = Integer.parseInt(prop.max());
								if (fld.getInt(null) > max){
									fld.setInt(max, max);
								}
							}else if(NumberUtils.isDigits(prop.min())){
								int min = Integer.parseInt(prop.min());
								if ( fld.getInt(null) < min){
									fld.setInt(min, min);
								}
							}
						}else if (Double.TYPE.equals(type)){
							p = config.get(prop.category(), fld.getName(), fld.getDouble(null));
							fld.setDouble(null, p.getDouble(0.0D));
							if ("" != prop.max()){
								try{
									double max = Double.parseDouble(prop.max());
									if (fld.getDouble(null) > max){
										fld.setDouble(max, max);
									}
								}catch(Exception ne){}
							}else if("" != prop.min()){
								try{
									double min = Double.parseDouble(prop.min());
									if ( fld.getDouble(null) < min){
										fld.setDouble(min, min);
									}
								}catch(Exception ne){}
							}
						}else if (String.class.equals(type)){
							p = config.get(prop.category(),fld.getName(),fld.get(null).toString());
							fld.set(null, p.getString());
						}else if (Boolean.TYPE.equals(type)){
							p = config.get(prop.category(),fld.getName(),fld.getBoolean(null));
							fld.setBoolean(null, p.getBoolean(fld.getBoolean(null)));
						}else{
						}
						if ((null != p) && (null != comment)) {
							p.setComment(comment);
						}
					} catch (IllegalArgumentException localIllegalArgumentException) {
					} catch (IllegalAccessException localIllegalAccessException) {
					}
				}
			}
		}

		if (isSave) {
			config.save();
		}
		_lastModify = _file.lastModified();
		_checkTime = System.currentTimeMillis();
	}

	public void saveConfig(){
		SavedConfig config = new SavedConfig(_file);
		config.load();
		for (Class<?> cls : _cls){
			Field[] fields = cls.getFields();
			for (Field fld : fields){
				ConfigProperty prop = (ConfigProperty)fld.getAnnotation(ConfigProperty.class);
				if ( prop == null){continue;}

				if (Modifier.isStatic(fld.getModifiers())){
					Class<?> type = fld.getType();
					Property p = null;
					try {
						String comment=I18n.translateToLocal(prop.comment());
						if (Integer.TYPE.equals(type)){
							p = config.get(prop.category(), fld.getName(), fld.getInt(null));
							if(prop.isSave()) config.set(prop.category(), fld.getName(), fld.getInt(null));
						}else if (Double.TYPE.equals(type)){
							p = config.get(prop.category(), fld.getName(), fld.getDouble(null));
							if(prop.isSave()) config.set(prop.category(), fld.getName(), fld.getDouble(null));
						}else if (String.class.equals(type)){
							p = config.get(prop.category(),fld.getName(),fld.get(null).toString());
							if(prop.isSave()) config.set(prop.category(), fld.getName(), fld.get(null).toString());
						}else if (Boolean.TYPE.equals(type)){
							p = config.get(prop.category(),fld.getName(),fld.getBoolean(null));
							if(prop.isSave()) config.set(prop.category(), fld.getName(), fld.getBoolean(null));
						}else{
						}
						if ((null != p) && (null != comment)) {
							p.setComment(comment);
						}
					} catch (IllegalArgumentException localIllegalArgumentException) {
					} catch (IllegalAccessException localIllegalAccessException) {
					}
				}
			}
		}
		config.save();
		_lastModify = _file.lastModified();
		_checkTime = System.currentTimeMillis();
	}
}
