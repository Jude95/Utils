package com.jude.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

        public Object readObjectFromFile(String name) {
            ObjectInputStream objectIn = null;
            Object object = null;
            FileInputStream fileIn = null;
            try {
                fileIn = new FileInputStream(getChildFile(name));
                objectIn = new ObjectInputStream(fileIn);
                object = objectIn.readObject();

            } catch (FileNotFoundException e) {
                // Do nothing
            }catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (objectIn != null) {
                    try {
                        objectIn.close();
                    } catch (IOException e) {
                        // do nowt
                    }
                }
                if(fileIn != null){
                    try {
                        fileIn.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return object;
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
