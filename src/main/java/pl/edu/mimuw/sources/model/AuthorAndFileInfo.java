package pl.edu.mimuw.sources.model;

public class AuthorAndFileInfo {

	private final String name;
	private final String author;
	private int count = 0;

	public int increaseCount() {
		count++;
		return count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + count;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorAndFileInfo other = (AuthorAndFileInfo) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (count != other.count)
			return false;
		return true;
	}

	public AuthorAndFileInfo(String name, String author) {
		super();
		this.name = name;
		this.author = author;
		this.count = 1;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public int getCount() {
		return count;
	}

}
