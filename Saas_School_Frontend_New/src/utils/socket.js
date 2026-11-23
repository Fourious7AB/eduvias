import { io } from 'socket.io-client';
import { addNotification } from './notifications/notificationsSlice';

let socket = null;

export const initializeSocket = (token, dispatch) => {
  const socketUrl = import.meta.env.VITE_REACT_BASE_URL_LOCAL;
  if (!socketUrl) {
    return null;
  }

  if (socket) {
    socket.disconnect();
  }
  
  socket = io(socketUrl, {
    auth: {
      token
    }
  });
  
  socket.on('connect', () => {
    console.log('Socket connected');
  });
  
  socket.on('disconnect', () => {
    console.log('Socket disconnected');
  });
  
  socket.on('error', (error) => {
    console.error('Socket error:', error);
  });
  
  socket.on('notification', (notification) => {
    dispatch(addNotification(notification));
  });
  
  return socket;
};

export const disconnectSocket = () => {
  if (socket) {
    socket.disconnect();
    socket = null;
  }
};

export const getSocket = () => socket;