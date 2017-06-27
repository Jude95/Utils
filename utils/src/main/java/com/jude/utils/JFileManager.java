package com.jude.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;

/**
 * Created by Mr.Jude on 2015/6/12.
 */
public class JFileManager {
    private static JFileManager instance;


    private HashMap<Enum,Folder> fileHashMap;



    public static JFileManager getInstance(){
        if (instance == null){
            instance = new JFileManager();
        }
        return instance;
    }

    public void init(Context ctx,Enum[] enums){
        File root = ctx.getFilesDir();
        fileHashMap = new HashMap<>();
        for (Enum dir: enums){
            File dirFile = new File(root,dir.name());
            if (!dirFile.exists())dirFile.mkdir();
            fileHashMap.put(dir,new Folder(dirFile));
        }
    }

    public Folder getFolder(Enum enumObj){
        return fileHashMap.get(enumObj);
    }
    public void clearAllData(){
        for (Folder folder:fileHashMap.values()){
            folder.clear();
        }
    }

    public class Folder{
        private File local;

        private Folder(File local){
            this.local = local;
        }

        public void clear(){
            fileDelete(local);
            local.mkdir();
        }

        public File getChildFile(String name){
            return new File(local,name);
        }

        public Folder getChildFolder(String name){
            return new Folder(new File(local,name));
        }

        public void deleteChild(String name){
            new File(local,name).delete();
        }

        public File getFile(){
            return local;
        }

        public File[] listChildFile(){
            return local.listFiles();
        }

        public String readStringFromFile(String name){
            File src = new File(local,name);
            Reader reader = null;
            try {
                reader = new FileReader(src);
                char[] flush = new char[10];
                int len = 0;
                StringBuilder sb = new StringBuilder();
                char a;
                Character.isLetter('a');
                while(-1!=(len = reader.read(flush)))
                {
                    sb.append(flush,0,len);
                }
                return sb.toString();
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
            finally
            {
                if(null!=reader)
                {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        public void writeStringToFile(String text,String name){
            File dest = new File(local,name);
            Writer wr = null;
            try {
                wr = new FileWriter(dest);
                wr.write(text);
                wr.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally
            {
                if(null!=wr)
                {
                    try {
                        wr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        public void writeObjectToFile(Object object, String name) {
            ObjectOutputStream objectOut = null;
            FileOutputStream fileOut = null;
            try {
                if(!getChildFile(name).exists()){
                    getChildFile(name).createNewFile();
                }
                fileOut = new FileOutputStream(getChildFile(name),false);
                objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(object);
                fileOut.getFD().sync();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }finally {
                if (objectOut != null) {
                    try {
                        objectOut.close();
                    } catch (IOException e) {
                        // do nowt
                    }
                }
                if (fileOut != null) {
                    try {
                        fileOut.close();
                    } catch (IOException e) {
                        // do nowt
                    }
                }
            }
        }

        public <T> T readObjectFromFile(String name) throws IOException{
            ObjectInputStream objectIn = null;
            Object object = null;
            FileInputStream fileIn = null;
            try {
                fileIn = new FileInputStream(getChildFile(name));
                objectIn = new ObjectInputStream(fileIn);
                object = objectIn.readObject();
            }catch (ClassNotFoundException e) {
                throw new IOException("ClassNotFoundException:"+e.getLocalizedMessage(),e);
            } finally {
                if (objectIn != null) {
                    objectIn.close();
                }
                if(fileIn != null){
                    fileIn.close();
                }
            }
            if (object != null){
                return (T) object;
            }
            throw new IOException("read error");
        }

        /**
         * 保存图片
         * @param bitmap
         * @param name
         */
        public void writeBitmapToFile(Bitmap bitmap, String name){
            deleteChild(name);
            File file = getChildFile(name);
            try {
                file.createNewFile();
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        /**
         * 删除文件，可删除文件夹
         * @param file
         */
        private void fileDelete(File file){
            if(file.isFile()){
                file.delete();
                return;
            }
            if(file.isDirectory()){
                File[] childFile = file.listFiles();
                if(childFile == null || childFile.length == 0){
                    file.delete();
                    return;
                }
                for(File f : childFile){
                    fileDelete(f);
                }
                file.delete();
            }
        }
    }
}
