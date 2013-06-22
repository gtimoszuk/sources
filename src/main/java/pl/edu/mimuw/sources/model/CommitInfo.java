package pl.edu.mimuw.sources.model;

import java.util.List;


public class CommitInfo {

	private final List<PathModel.PathChangeModel> files;
	private final String author;
	private final String message;
	private final int time;

	public CommitInfo(List<PathModel.PathChangeModel> files, String author, String message, int time) {
		this.files = files;
		this.author = author;
		this.message = message;
		this.time = time;
	}

	public List<PathModel.PathChangeModel> getFiles() {
		return files;
	}

	public String getAuthor() {
		return author;
	}

	public String getMessage() {
		return message;
	}

	public long getTime() {
		return new Long(time) * 1000;
	}
}
