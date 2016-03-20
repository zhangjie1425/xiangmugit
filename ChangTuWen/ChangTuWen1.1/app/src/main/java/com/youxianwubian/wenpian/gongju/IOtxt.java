package com.youxianwubian.wenpian.gongju;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IOtxt
{
	/**
	 　　* 保存文件
	 　　* @param toSaveString
	 　　* @param filePath
	 　　*/
	public static boolean saveFile(String toSaveString, String filePath)
	{
		try
		{
			File saveFile = new File(filePath);
			if (!saveFile.exists())
			{
				File dir = new File(saveFile.getParent());
				dir.mkdirs();
				saveFile.createNewFile();
			}

			FileOutputStream outStream = new FileOutputStream(saveFile);
			outStream.write(toSaveString.getBytes());
			outStream.close();
			return true;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}


}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}

	}
	/**
	 * 获取SDCard的目录路径功能
	 * @return
	 */
	public static String getSDCardPath(){
		File sdcardDir = null;
		//判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(sdcardExist){
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}
public static String baocuntp(Context context)
{
	try {
	File path = new File(getSDCardPath() + "/wenpian/zuopin/");
	if(!path.exists()){
		path.mkdirs();
	}
	}
	catch (Exception e) {
		System.out.println("创建文件夹操作出错");
		e.printStackTrace();

	}
	String fileName = System.currentTimeMillis() + ".jpg";
	String filelj=getSDCardPath() + "/wenpian/zuopin/"+fileName;
	copyFile(getSDCardPath() + "/wenpian/tp/tp.jpg",filelj);
	//saveFile(filelj,getSDCardPath() + "/wenpian/tp/tp.jpg");
	// 其次把文件插入到系统图库
	try {
		MediaStore.Images.Media.insertImage(context.getContentResolver(),
				getSDCardPath() + "/wenpian/zuopin/", fileName, null);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	// 最后通知图库更新
	context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filelj)));

	return filelj;
}

/*
	public static void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
	}*/
	/**
	 　　* 读取文件内容
	 　　* @param filePath
	 　　* @return 文件内容
	 　　*/
	public static String readFile(String filePath)
	{
		String str = "";
		try
		{
			File readFile = new File(filePath);
			if(!readFile.exists())
			{
				return null;
			}
			FileInputStream inStream = new FileInputStream(readFile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1)
			{
				stream.write(buffer, 0, length);
			}
			str = stream.toString();
			stream.close();
			inStream.close();
			return str;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 递归删除文件和文件夹
	 * @param file    要删除的根目录
	 */
	public static void RecursionDeleteFile(File file){
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
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	//获取该路径下图片
	public static List<String> gettp(File file) {
		//从根目录开始扫描
		//	Log.i(TAG, file.getPath());
		List<String> fileList = new ArrayList<String>();
		getFileList(file, fileList);
		return fileList;
	}
	/**
	 * @param path
	 * @param fileList
	 * 注意的是并不是所有的文件夹都可以进行读取的，权限问题
	 */
	private static void getFileList(File path, List<String> fileList){
		//如果是文件夹的话
		if(path.isDirectory()){
			//返回文件夹中有的数据
			File[] files = path.listFiles();
			//先判断下有没有权限，如果没有权限的话，就不执行了
			if(null == files)
				return;

			for(int i = 0; i < files.length; i++){
				getFileList(files[i], fileList);
			}
		}
		//如果是文件的话直接加入
		else{
			//Log.i(TAG, path.getAbsolutePath());
			if(checkIsImageFile(path.getName()))
			{
				//进行文件的处理
				String filePath = path.getAbsolutePath();
				//文件名
				//String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
				//添加
				fileList.add(filePath);
			}
		}
	}
	// 检查扩展名，得到图片格式的文件
	private static boolean checkIsImageFile(String fName) {
		boolean isImageFile = false;
		// 获取扩展名
		String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
				fName.length()).toLowerCase();
		if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("jpeg")) {
			isImageFile = true;
		} else {
			isImageFile = false;
		}
		return isImageFile;
	}
}
