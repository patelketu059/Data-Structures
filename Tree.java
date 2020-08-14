package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		
		root = buildRecursion();
	}
		
	
	private TagNode buildRecursion() {
		boolean checkMore = false;
		String currTag = null;
		if(sc.hasNextLine())
		{
			currTag = sc.nextLine();
		}
		else
			return null;
		
		if(currTag.charAt(0) == '<')
		{
			currTag = currTag.substring(1, currTag.indexOf('>'));
			if(currTag.charAt(0) == '/')
			{
				return null;
			}
			else
				checkMore = true;
		}
		
		TagNode temp = new TagNode(currTag, null, null);
		if(checkMore)
			temp.firstChild = buildRecursion();
			
		temp.sibling = buildRecursion();
		return temp;
		
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		replaceTag(root.firstChild, oldTag, newTag);
	}
	
	private void replaceTag(TagNode root, String oldTag, String newTag)
	{
		TagNode curr = root.firstChild;
		if(curr.tag.matches(oldTag) && curr.firstChild != null) 
		{
			curr.tag = newTag;
		}
		
		while(curr != null)
		{
			if(curr.firstChild != null)
			{
				replaceTag(curr, oldTag, newTag);
			}
			if(curr.tag.matches(oldTag) && curr.firstChild != null)
			{
				curr.tag = newTag;
			}
			curr = curr.sibling;
		}
		return;
	}
	
	
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		if(row < 1)
		{
			return;
		}
		boldRecursion(root, null, 1, row);
	}
	
	private void boldRecursion(TagNode curr, TagNode prevCurr, int currRow, int row) {
		if(curr == null)
		{
			return;
		}
		if(currRow != row) 
		{
			currRow++;	
		}
		else if(currRow == row && curr.firstChild == null)
		{
			prevCurr.firstChild = new TagNode("b", curr, null);
		}
		boldRecursion(curr.firstChild, curr, currRow, row);
		boldRecursion(curr.sibling, curr, currRow, row);
	}
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		removeTag(root, null, tag);
	}
	
	private void removeTag(TagNode root, TagNode prev, String tag)
	{
		TagNode curr = root;
		TagNode prevCurr = prev;
		while(curr != null)
		{
			if(curr.tag.matches(tag))
			{
				if(curr.tag.matches("p") || curr.tag.matches("b") || curr.tag.matches("em"))
				{
					if(prev.firstChild == curr)
					{
						TagNode childCurr = curr.firstChild;
						while(childCurr.sibling != null)
						{
							childCurr = childCurr.sibling;
						}
						childCurr.sibling = curr.sibling;
						prev.firstChild = curr.firstChild;
						curr = curr.firstChild;
					}
					else
					{
						TagNode childCurr = curr.firstChild;
						while(childCurr.sibling != null)
						{
							childCurr = childCurr.sibling;
						}
						childCurr.sibling = curr.sibling;
						prev.sibling = curr.firstChild;
						curr = curr.firstChild;
					}
					removeTag(curr, prevCurr, tag);
				}
				else
				{
					if(prev.firstChild == curr)
					{
						TagNode childCurr = curr.firstChild;
						while(childCurr.sibling != null)
						{
							childCurr.tag = "p";
							childCurr = childCurr.sibling;
						}
						childCurr.tag = "p";
						childCurr.sibling = curr.sibling;
						prev.firstChild = curr.firstChild;
						curr = curr.firstChild;
					}
					else
					{
						TagNode childCurr = curr.firstChild;
						while(childCurr.sibling != null)
						{
							childCurr.tag = "p";
							childCurr = childCurr.sibling;
						}
						childCurr.tag = "p";
						childCurr.sibling = curr.sibling;
						prev.sibling = curr.firstChild;
						curr = curr.firstChild;
					}
					removeTag(curr, prevCurr, tag);
				}
			}
			else if(curr.firstChild != null)
			{
				removeTag(curr.firstChild, curr, tag);
			}
			prev = curr;
			curr = curr.sibling;	
		}
		return;
	}

	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		addRecursion(word, tag, root);
	}
	
	
	private void addRecursion(String word, String tag, TagNode curr) {
		if(curr == null) return;
		addRecursion(word, tag, curr.firstChild);
		
		if(curr.firstChild != null && curr.firstChild.tag.contains(word)) 
		{
			String[] contains = curr.firstChild.tag.split(word);
			if(contains.length == 0) 
			{
				TagNode newTag = new TagNode(tag, curr.firstChild, curr.sibling);
				curr.firstChild = newTag;
			}
			else if(contains.length == 2) 
			{
				TagNode afterWord = new TagNode(contains[1], null, curr.firstChild.sibling);
				TagNode beforeWord = new TagNode(contains[0], null, null);
				TagNode original = new TagNode(word, null, null);
				TagNode newTag = new TagNode(tag, original, afterWord);
				curr.firstChild = beforeWord;
				beforeWord.sibling = newTag;
				if(contains[0].equals("")) 
					{
						curr.firstChild = newTag;	
					}
			}
			else {
				if(contains[0].charAt(0) == ' ') 
				{
					TagNode original = new TagNode(word, null, null);
					TagNode newTag = new TagNode(tag, original, curr.firstChild.sibling);
					TagNode afterWord = new TagNode(contains[0], null, curr.firstChild.sibling);
					newTag.sibling = afterWord;
					curr.firstChild = newTag;
					
				}
				else
				{
					
					TagNode original = new TagNode(word, null, null);
					TagNode newTag = new TagNode(tag, original, curr.firstChild.sibling);
					TagNode beforeWord = new TagNode(contains[0], null, newTag);
					curr.firstChild = beforeWord;
					
				}
				
			}
			
		}
		if(curr.sibling != null && curr.sibling.tag.contains(word))
		{
			String[] contains = curr.sibling.tag.split(word);
			if(contains.length == 0)
			{
				TagNode newTag = new TagNode(tag, curr.sibling, curr.sibling.sibling);
				curr.sibling = newTag;
			}
			else if(contains.length > 0)
			{
				TagNode afterWord = new TagNode(contains[1], null, curr.sibling.sibling);
				TagNode beforeWord = new TagNode(contains[0], null, null);
				TagNode original = new TagNode(word, null, null);
				TagNode newTag = new TagNode(tag, original, afterWord);
				curr.sibling = beforeWord;
				beforeWord.sibling = newTag;
				newTag.sibling = afterWord;
			}
			else
			{
				TagNode original = new TagNode(word, null, null);
				TagNode newTag = new TagNode(tag, original, null);
				if(contains[0].charAt(0) == ' ')
				{
					TagNode afterWord = new TagNode(contains[0], null, curr.sibling.sibling);
					newTag.sibling = afterWord;
					curr.sibling = newTag;
				}
				else 
				{
					TagNode beforeWord = new TagNode(contains[0], null, newTag);
					curr.sibling = beforeWord;
				}
			}
		}
		addRecursion(word, tag, curr.sibling);
		
	}
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
