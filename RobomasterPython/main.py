import socket
import pyautogui as gui
import queue
import threading
import win10toast
import ctypes

# Global parameters
MESSAGES_QUEUE = queue.Queue()
SERVER_SOCK = 12346
CENTER_X = 649
CENTER_Y = 377
TOASTER = win10toast.ToastNotifier()


def erase(amount: int):
    """
    The function will erase all text on the screen according to the commands sent
    Parameters:
        amount (int): amount of commands the user sent
    """
    with gui.hold('shift'):
        for i in range(0, amount):
            gui.press('up')
    gui.press('backspace')
    gui.hotkey('ctrl', 'a')
    gui.keyDown('backspace')
    gui.keyUp('backspace')


def reset(amount: int):
    """
    The function will reset the screen to an empty and text-less state
    Parameters:
        amount (int): amount of commands the user sent
    """
    gui.click(CENTER_X, CENTER_Y)
    erase(amount)
    erase(amount)


def is_idle() -> bool:
    """
    The function will return if the run button was found and we can execute new commands
    Returns:
        bool: true if the run button was found, false otherwise
    """
    try:
        return gui.locateOnScreen('runBtn.png', confidence=0.5) is not None
    except:
        return False


def execute_message_thread():
    """
    The function will execute commands from the message queue and reset the screen
    This function runs on a thread which means that it is ALWAYS running and checking if a new
    set of commands was added to the queue
    """
    while True:
        if not MESSAGES_QUEUE.empty():
            pass
        if not MESSAGES_QUEUE.empty() and is_idle():
            client_message = MESSAGES_QUEUE.get()
            amount = client_message.count('\n')
            typeNew(client_message)
            run()
            erase(amount)
            reset(amount)
            MESSAGES_QUEUE.task_done()
            TOASTER.show_toast("Messages left: " + str(MESSAGES_QUEUE.qsize()))


def run():
    """
    The function will locate the run button and will click it
    """
    try:
        btn_start = gui.locateOnScreen('runBtn.png')
        x, y = gui.center(btn_start)
        gui.click(x, y)
    except:
        return



def typeNew(command: str):
    """
    The function will type a new set of commands to the screen
    Parameters:
        command (str): a new set of commands to be executed
    """
    gui.press('enter')
    gui.typewrite(command)





def main():
    """
    The main function will start the server and the thread which will execute commands
    """
    host = socket.gethostname()
    ip = socket.gethostbyname(host)
    print("IP: " + ip)
    print("port: " + str(SERVER_SOCK))

    # Start the server
    listening_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    listening_socket.bind((ip, SERVER_SOCK))
    listening_socket.listen()

    # Start the execution thread
    worker_thread = threading.Thread(target=execute_message_thread, daemon=True)
    worker_thread.start()

    print("Waiting")
    client_sock, client_addr = listening_socket.accept()

    with client_sock:
        gui.click(CENTER_X, CENTER_Y)
        print("Connected")
        while True:
            # Get message from the client
            msg_length = int.from_bytes(client_sock.recv(4), byteorder="big", signed=False)
            if msg_length:
                command = client_sock.recv(msg_length).decode()
                MESSAGES_QUEUE.put(command)  # Add commands to the execution queue


if __name__ == "__main__":
    main()
