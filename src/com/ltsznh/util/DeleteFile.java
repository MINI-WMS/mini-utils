package com.ltsznh.util;

import java.io.File;

public class DeleteFile {
	/**
	 * 删除空目录
	 * 
	 * @param dir
	 *            将要删除的目录路径
	 */
	public void doDeleteEmptyDir(String dir) {
		boolean success = (new File(dir)).delete();
		if (success) {
			System.out.println("Successfully deleted empty directory: " + dir);
		} else {
			System.out.println("Failed to delete empty directory: " + dir);
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 此文件为文件或为空目录时，可以删除
		System.out.println("删除：" + dir.getAbsolutePath());
		return dir.delete();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new DeleteFile().doDeleteEmptyDir("new_dir1");
		String newDir2 = "new_dir2";
		boolean success = new DeleteFile().deleteDir(new File(newDir2));
		if (success) {
			System.out.println("Successfully deleted populated directory: "
					+ newDir2);
		} else {
			System.out.println("Failed to delete populated directory: "
					+ newDir2);
		}
	}

}
