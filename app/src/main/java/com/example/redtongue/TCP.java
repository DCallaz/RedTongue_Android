package com.example.redtongue;

import android.widget.ExpandableListAdapter;

import java.net.*;
import java.io.*;
import java.nio.*;

public class TCP {
	public static final boolean SEND = true;
	public static final boolean RECV = false;

	private boolean mode;
	private ServerSocket serv;
	private Socket sock;
	private DataInputStream in;
	private DataOutputStream out;
	private boolean setup_complete;

	/**
	 * Sender constructor. Constructs a sender TCP object for transfer. Once this constructor returns,
	 * the full sender object is ready.
	 * @param host: The hostname of the device to connect to.
	 * @param port: The port of the device to connect to.
	 */
	public TCP(String host, int port) {
		this.setup_complete = false;
		this.mode = SEND;
		if (host == null) {
			host = "localhost";
		}
		try {
			sock = new Socket(host, port);
			sock.setReuseAddress(true);
		} catch (IOException e) {
			System.out.println("Unable to open sender socket: "+e);
		}
		setIO();
		this.setup_complete = true;
	}

	/**
	 * Receiver constructor. Constructs a receiver TCP object for transfer.
	 * NOTE: The <code>connect()</code> method must be called after this constructor to complete the
	 * setup for the receiver.
	 * @param port: The port for the connection.
	 */
	public TCP(int port) {
		this.setup_complete = false;
		this.mode = RECV;
		try {
			serv = new ServerSocket(port);
			serv.setReuseAddress(true);
		} catch (IOException e) {
			System.out.println("Unable to open server (receiver) socket. "+e);
			e.printStackTrace();
		}
		System.out.println("Created new receiver object");
	}

	/**
	 * Constructs the IO objects. Will be called either from the sender constructor or after a connection
	 * has been accepted in the receiver's <code>connect()</code> method.
	 */
	private void setIO() {
		try {
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			System.out.println("Could not start readers and writers");
		}
	}

	/**
	 * Blocks for a connection to be established between this object and a sender TCP object. Once a
	 * connection is established, setup of the IO objects is done.
	 */
	public void connect() {
		try {
			sock = serv.accept();
			sock.setReuseAddress(true);
			setIO();
			this.setup_complete = true;
		} catch (SocketException e) {
			System.out.println("Got socket exception while blocking for receive: "+e);
		} catch (IOException e) {
			System.out.println("Unable to open server (receiver) socket. "+e);
			e.printStackTrace();
		}
	}

  public int getPort() {
    if (serv != null) {
      return serv.getLocalPort();
    }
    return -1;
  }

	public void send(byte[] bytes, int length, Progress prog, int chunk) throws Exception {
		if (this.mode != SEND) {
			throw new Exception("Incompatible mode, receiver can't run send");
		}
		if (!setup_complete) {
			throw new Exception("TCP sender not fully setup before call to send!");
		}
		int size = (length == -1) ? bytes.length : length;
		int index = 0;
		System.out.println("Size: "+size+" chunk: "+chunk);
		out.writeInt(size);
		out.writeInt(chunk);
		while (index + chunk < size) {
			out.write(bytes, index, chunk);
			index += chunk;
			if (prog != null) {
				double div = (double)index/size;
				prog.updateProgress((short)(div*100));
			}
		}
		out.write(bytes, index, size - index);
		if (prog != null) {
			prog.updateProgress((short)100);
		}
	}

	public byte[] recv(Progress prog) throws Exception {
		if (this.mode != RECV) {
			throw new Exception("Incompatible mode, sender can't run receive");
		}
		if (!setup_complete) {
			throw new Exception("TCP receiver not fully setup before call to send!");
		}
		int size = in.readInt();
		int chunk = in.readInt();
		System.out.println("Size: "+size+" chunk: "+chunk);
		int index = 0;
		//System.out.println("Size: "+size+" Chunk: "+chunk);
		byte[] ret = new byte[size];
		while (index + chunk < size) {
			int read = 0;
      while (read < chunk) {
        read += in.read(ret, index+read, chunk-read);
      }
      //in.read(ret, index, chunk);
			index += chunk;
			if (prog != null) {
				double div = (double)index/size;
				prog.updateProgress((short)(div*100));
			}
		}
    int read = 0;
    while (read < size-index) {
      read += in.read(ret, index+read, (size-index)-read);
    }
		//in.read(ret, index, size-index);
		if (prog != null) {
			prog.updateProgress((short)100);
		}
		return ret;
	}

	public void sendSize(int size) {
		try {
			out.writeInt(size);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public int recvSize() {
		try {
			return in.readInt();
		} catch (IOException e) {
			System.out.println(e);
		}
		return -1;
	}

	public void sendName(String path) {
    File file = new File(path);
    String name = file.getName();

		try {
			out.writeUTF(name);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public String recvName() {
		try {
			return in.readUTF();
		} catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}

  public void close() {
    try {
      if (serv != null) {
        serv.close();
      }
      if (sock != null) {
		  sock.close();
	  }
    } catch (IOException e) {
      System.out.println("Could not close sockets");
    }
  }

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("USAGE: TCP <mode 0/1>");
			System.exit(0);
		}
		int modei = Integer.parseInt(args[0]);
		boolean mode = (modei == 0) ? RECV : SEND;
		TCP t;
		if (mode == SEND) {
			t = new TCP(null, 8199);
		} else {
			t = new TCP(8199);
			t.connect();
		}
		try {
			if (mode == SEND) {
				if (args.length > 1) {
					t.send(args[1].getBytes(), -1, new TuiProgress(), 1);
				} else {
					t.send("Hello, world!".getBytes(), -1, new TuiProgress(), 1);
				}
			} else {
				byte[] ret;
				ret = t.recv(new TuiProgress());
				System.out.println(new String(ret, "UTF-8"));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
