package org.bitenet.lang2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class Slave implements  KeyListener, MouseMotionListener, MouseListener{
boolean top;
Memory m;
LineReader in;
BufferedImage img;
public static final Integer[] KEYMAP = {KeyEvent.VK_0,KeyEvent.VK_1,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_4,KeyEvent.VK_5,KeyEvent.VK_6,KeyEvent.VK_7,KeyEvent.VK_8,KeyEvent.VK_9,KeyEvent.VK_A,KeyEvent.VK_ACCEPT,KeyEvent.VK_ADD,KeyEvent.VK_AGAIN,KeyEvent.VK_ALL_CANDIDATES,KeyEvent.VK_ALPHANUMERIC,KeyEvent.VK_ALT,KeyEvent.VK_ALT_GRAPH,KeyEvent.VK_AMPERSAND,KeyEvent.VK_ASTERISK,KeyEvent.VK_AT,KeyEvent.VK_B,KeyEvent.VK_BACK_QUOTE,KeyEvent.VK_BACK_SLASH,KeyEvent.VK_BACK_SPACE,KeyEvent.VK_BEGIN,KeyEvent.VK_BRACELEFT,KeyEvent.VK_BRACERIGHT,KeyEvent.VK_C,KeyEvent.VK_CANCEL,KeyEvent.VK_CAPS_LOCK,KeyEvent.VK_CIRCUMFLEX,KeyEvent.VK_CLEAR,KeyEvent.VK_CLOSE_BRACKET,KeyEvent.VK_CODE_INPUT,KeyEvent.VK_COLON,KeyEvent.VK_COMMA,KeyEvent.VK_COMPOSE,KeyEvent.VK_CONTEXT_MENU,KeyEvent.VK_CONTROL,KeyEvent.VK_CONVERT,KeyEvent.VK_COPY,KeyEvent.VK_CUT,KeyEvent.VK_D,KeyEvent.VK_DEAD_ABOVEDOT,KeyEvent.VK_DEAD_ABOVERING,KeyEvent.VK_DEAD_ACUTE,KeyEvent.VK_DEAD_BREVE,KeyEvent.VK_DEAD_CARON,KeyEvent.VK_DEAD_CEDILLA,KeyEvent.VK_DEAD_CIRCUMFLEX,KeyEvent.VK_DEAD_DIAERESIS,KeyEvent.VK_DEAD_DOUBLEACUTE,KeyEvent.VK_DEAD_GRAVE,KeyEvent.VK_DEAD_IOTA,KeyEvent.VK_DEAD_MACRON,KeyEvent.VK_DEAD_OGONEK,KeyEvent.VK_DEAD_SEMIVOICED_SOUND,KeyEvent.VK_DEAD_TILDE,KeyEvent.VK_DEAD_VOICED_SOUND,KeyEvent.VK_DECIMAL,KeyEvent.VK_DELETE,KeyEvent.VK_DIVIDE,KeyEvent.VK_DOLLAR,KeyEvent.VK_DOWN,KeyEvent.VK_E,KeyEvent.VK_END,KeyEvent.VK_ENTER,KeyEvent.VK_EQUALS,KeyEvent.VK_ESCAPE,KeyEvent.VK_EURO_SIGN,KeyEvent.VK_EXCLAMATION_MARK,KeyEvent.VK_F,KeyEvent.VK_F1,KeyEvent.VK_F10,KeyEvent.VK_F11,KeyEvent.VK_F12,KeyEvent.VK_F13,KeyEvent.VK_F14,KeyEvent.VK_F15,KeyEvent.VK_F16,KeyEvent.VK_F17,KeyEvent.VK_F18,KeyEvent.VK_F19,KeyEvent.VK_F2,KeyEvent.VK_F20,KeyEvent.VK_F21,KeyEvent.VK_F22,KeyEvent.VK_F23,KeyEvent.VK_F24,KeyEvent.VK_F3,KeyEvent.VK_F4,KeyEvent.VK_F5,KeyEvent.VK_F6,KeyEvent.VK_F7,KeyEvent.VK_F8,KeyEvent.VK_F9,KeyEvent.VK_FINAL,KeyEvent.VK_FIND,KeyEvent.VK_FULL_WIDTH,KeyEvent.VK_G,KeyEvent.VK_GREATER,KeyEvent.VK_H,KeyEvent.VK_HALF_WIDTH,KeyEvent.VK_HELP,KeyEvent.VK_HIRAGANA,KeyEvent.VK_HOME,KeyEvent.VK_I,KeyEvent.VK_INPUT_METHOD_ON_OFF,KeyEvent.VK_INSERT,KeyEvent.VK_INVERTED_EXCLAMATION_MARK,KeyEvent.VK_J,KeyEvent.VK_JAPANESE_HIRAGANA,KeyEvent.VK_JAPANESE_KATAKANA,KeyEvent.VK_JAPANESE_ROMAN,KeyEvent.VK_K,KeyEvent.VK_KANA,KeyEvent.VK_KANA_LOCK,KeyEvent.VK_KANJI,KeyEvent.VK_KATAKANA,KeyEvent.VK_KP_DOWN,KeyEvent.VK_KP_LEFT,KeyEvent.VK_KP_RIGHT,KeyEvent.VK_KP_UP,KeyEvent.VK_L,KeyEvent.VK_LEFT,KeyEvent.VK_LEFT_PARENTHESIS,KeyEvent.VK_LESS,KeyEvent.VK_M,KeyEvent.VK_META,KeyEvent.VK_MINUS,KeyEvent.VK_MODECHANGE,KeyEvent.VK_MULTIPLY,KeyEvent.VK_N,KeyEvent.VK_NONCONVERT,KeyEvent.VK_NUM_LOCK,KeyEvent.VK_NUMBER_SIGN,KeyEvent.VK_NUMPAD0,KeyEvent.VK_NUMPAD1,KeyEvent.VK_NUMPAD2,KeyEvent.VK_NUMPAD3,KeyEvent.VK_NUMPAD4,KeyEvent.VK_NUMPAD5,KeyEvent.VK_NUMPAD6,KeyEvent.VK_NUMPAD7,KeyEvent.VK_NUMPAD8,KeyEvent.VK_NUMPAD9,KeyEvent.VK_O,KeyEvent.VK_OPEN_BRACKET,KeyEvent.VK_P,KeyEvent.VK_PAGE_DOWN,KeyEvent.VK_PAGE_UP,KeyEvent.VK_PASTE,KeyEvent.VK_PAUSE,KeyEvent.VK_PERIOD,KeyEvent.VK_PLUS,KeyEvent.VK_PREVIOUS_CANDIDATE,KeyEvent.VK_PRINTSCREEN,KeyEvent.VK_PROPS,KeyEvent.VK_Q,KeyEvent.VK_QUOTE,KeyEvent.VK_QUOTEDBL,KeyEvent.VK_R,KeyEvent.VK_RIGHT,KeyEvent.VK_RIGHT_PARENTHESIS,KeyEvent.VK_ROMAN_CHARACTERS,KeyEvent.VK_S,KeyEvent.VK_SCROLL_LOCK,KeyEvent.VK_SEMICOLON,KeyEvent.VK_SEPARATOR,KeyEvent.VK_SHIFT,KeyEvent.VK_SLASH,KeyEvent.VK_SPACE,KeyEvent.VK_STOP,KeyEvent.VK_SUBTRACT,KeyEvent.VK_T,KeyEvent.VK_TAB,KeyEvent.VK_U,KeyEvent.VK_UNDEFINED,KeyEvent.VK_UNDERSCORE,KeyEvent.VK_UNDO,KeyEvent.VK_UP,KeyEvent.VK_V,KeyEvent.VK_W,KeyEvent.VK_WINDOWS,KeyEvent.VK_X,KeyEvent.VK_Y,KeyEvent.VK_Z};
ArrayList<Integer> keymap;
ArrayList<String> nativeComs;
GraphicUpdater imagerec;
public static final String REMOTE_IDENTIFIER = "RR_";
Remote r;
public static final String[] NATIVE = {"goto","set","add","mult","div","sub","pix","gflush","<","=","!"};
	public Slave(Memory space, boolean topp, String def, Remote rr, BufferedImage graph) throws FileNotFoundException {
		in = new LineReader(def);
		this.top = topp;
		r = rr;
		img = graph;
		nativeComs = toList(NATIVE);
		keymap = toList(KEYMAP);
	}
	public <T> ArrayList<T> toList(T[] t){
		ArrayList<T> ret = new ArrayList<T>();
		for (int i = 0; i < t.length; i++) {
			ret.add(t[i]);
		}
		return ret;
	}
	public Memory activate() throws FileNotFoundException {
		String line;
		while((line=in.nextLine())!=null) {
			if(isNative(line)) {
				runNative(line);
			}else {
				if(line.startsWith(REMOTE_IDENTIFIER)&&r!=null) {
					String[] com = line.split(" ");
					String func = com[0];
					int[] argPointers = extendList(com[1]);
					int space = Integer.parseInt(com[2]);
					int resStart = Integer.parseInt(com[3]);
					int[] resPointers = extendList(com[4]);
					Memory nMem = new Memory(argPointers.length+space);
					SlaveDefinition sd = new SlaveDefinition(func,nMem);
					if(r.contains(sd)) {
						nMem  = r.retrieve(sd);
						for (int i = resStart; i < resPointers.length; i++) {
							m.set(i,nMem.get(resPointers[i]));
						}
					}else {
						run(line);
					}
				}else {
					run(line);
				}
			}
		}
		return m;
	}
	private void runNative(String l) {
		if(l.startsWith("set")) {
			String[] k = l.split(" ");
			m.set(Integer.parseInt(k[1]), valueOf(k[2]));
		}else if(l.startsWith("add")) {
			String[] k = l.split(" ");
			m.set(Integer.parseInt(k[3]), (valueOf(k[1])+valueOf(k[2])));
		}else if(l.startsWith("sub")) {
			String[] k = l.split(" ");
			m.set(Integer.parseInt(k[3]), (valueOf(k[1])-valueOf(k[2])));
		}else if(l.startsWith("mult")) {
			String[] k = l.split(" ");
			m.set(Integer.parseInt(k[3]), (valueOf(k[1])*valueOf(k[2])));
		}else if(l.startsWith("div")) {
			String[] k = l.split(" ");
			m.set(Integer.parseInt(k[3]), (valueOf(k[1])/valueOf(k[2])));
		}else if(l.startsWith("pix")) {
			if(top) {
			String[] k = l.split(" ");
			int x = valueOf(k[1]);
			int y = valueOf(k[2]);
			int c = valueOf(k[3]);
			img.setRGB(x, y, c);
			}
		}else if(l.startsWith("gflush")) {
			if(top) {
			imagerec.onUpdate(img);
			}
		}else if(l.startsWith("print")) {
			String[] k = l.split(" ");
			System.out.println(valueOf(k[1]));
		}else if(l.startsWith("<")) {
			String[] k = l.split(" ");
			m.set(Integer.parseInt(k[3]), (valueOf(k[1])<valueOf(k[2])?1:0));
		}else if(l.startsWith("=")) {
			String[] k = l.split(" ");
			m.set(Integer.parseInt(k[3]), (valueOf(k[1])==valueOf(k[2])?1:0));
		}else if(l.startsWith("!")) {
			String[] k = l.split(" ");
			m.set(Integer.parseInt(k[3]), (valueOf(k[1])!=valueOf(k[2])?1:0));
		}else if(l.startsWith("goto")) {
			String[] k = l.split(" ");
			in.setLine(valueOf(k[1]));
		}
		
	}
	private void run(String line) throws FileNotFoundException {
		String[] com = line.split(" ");
		String func = com[0];
		int[] argPointers = extendList(com[1]);
		int space = Integer.parseInt(com[2]);
		int resStart = Integer.parseInt(com[3]);
		int[] resPointers = extendList(com[4]);
		Memory nMem = new Memory(argPointers.length+space);
		for (int i = 0; i < argPointers.length; i++) {
			nMem.set(i, argPointers[i]);
		}
		nMem = (new Slave(nMem,false,func,r,img)).activate();
		for (int i = resStart; i < resPointers.length; i++) {
			m.set(i,nMem.get(resPointers[i]));
		}
	}
	private int[] extendList(String list) {
		String[] nums = list.split(",");
		ArrayList<Integer> rett = new ArrayList<Integer>();
		for (int i = 0; i < nums.length; i++) {
			if(nums[i].contains("-")) {
				String[] range = nums[i].split("-");
				int start = Integer.parseInt(range[0]);
				int end = Integer.parseInt(range[1]);
				for (int j = start; j <= end; j++) {
					rett.add(j);
				}
			}else {
				rett.add(Integer.parseInt(nums[i]));
			}
		}
		int[] ret = new int[rett.size()];
		for (int i = 0; i < rett.size(); i++) {
			ret[i] = rett.get(i);
		}
		return ret;
	}
	private int valueOf(String s) {
		if(s.startsWith("%")) {
			return m.get(Integer.parseInt(s.substring(1,s.length()-1)));
		}
		return Integer.parseInt(s);
	}
	private boolean isNative(String l) {
		return nativeComs.contains(l);
	}
	private int getKeyValue(KeyEvent e) {
		return keymap.indexOf(e.getKeyCode());

}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void mouseDragged(MouseEvent arg0) {}
	@Override
	public void mouseMoved(MouseEvent e) {
		if(top) {
			m.set(0,e.getX());
			m.set(1,e.getY());
		}
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(top) {
			m.set(2+getKeyValue(e),1);
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(top) {
			m.set(2+getKeyValue(e),0);
		}
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
