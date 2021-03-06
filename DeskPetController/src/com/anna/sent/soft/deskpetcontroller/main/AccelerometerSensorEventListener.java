package com.anna.sent.soft.deskpetcontroller.main;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class AccelerometerSensorEventListener implements SensorEventListener,
		MotionListener {
	public static interface LogListener {
		public void log(double Gx, double Gy, double Gz, long timestamp,
				int accuracy);
	}

	private LogListener mLogListener;
	private List<MotionListener> mMotionListeners = new ArrayList<MotionListener>();

	public AccelerometerSensorEventListener() {
	}

	public void setLogListener(LogListener listener) {
		mLogListener = listener;
	}

	public void addListener(MotionListener listener) {
		mMotionListeners.add(listener);
	}

	public void removeListener(MotionListener listener) {
		mMotionListeners.remove(listener);
	}

	private long mLastTimestamp = 0;
	private final static long TIME_ELAPSED = 1000000000; // 1 second, in
															// nanoseconds

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.timestamp - mLastTimestamp < TIME_ELAPSED) {
			return;
		}

		mLastTimestamp = event.timestamp;
		double Gx = event.values[0], sqr_Gx = Gx * Gx;
		double Gy = event.values[1], sqr_Gy = Gy * Gy;
		double Gz = event.values[2], sqr_Gz = Gz * Gz;
		double sqr_mod_G = sqr_Gx + sqr_Gy + sqr_Gz;
		double rel_Gx = sqr_Gx / sqr_mod_G;
		double rel_Gy = sqr_Gy / sqr_mod_G;
		double rel_Gz = sqr_Gz / sqr_mod_G;
		if (rel_Gx > 0.9) {
			if (Gx > 0) {
				rotateLeft();
			} else if (Gx < 0) {
				rotateRight();
			}
		} else if (rel_Gz < 0.5 && Gz > 0 && rel_Gy < 0.9 && Gy > 0) {
			if (rel_Gx < 0.1) {
				moveBackward();
			} else if (Gx > 0) {
				moveToLeftBackward();
			} else if (Gx < 0) {
				moveToRightBackward();
			}
		} else if (rel_Gz > 0.9 && Gz < 0) {
			stop();
		} else if (rel_Gz > 0.5 && Gz > 0 && rel_Gy < 0.9) {
			if (rel_Gx < 0.1) {
				moveForward();
			} else if (Gx > 0) {
				moveToLeftForward();
			} else if (Gx < 0) {
				moveToRightForward();
			}
		} else if (rel_Gy > 0.9) {
			stop();
		}

		if (mLogListener != null) {
			mLogListener.log(Gx, Gy, Gz, event.timestamp, event.accuracy);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void moveForward() {
		for (MotionListener listener : mMotionListeners) {
			listener.moveForward();
		}
	}

	@Override
	public void moveBackward() {
		for (MotionListener listener : mMotionListeners) {
			listener.moveBackward();
		}
	}

	@Override
	public void moveToLeftForward() {
		for (MotionListener listener : mMotionListeners) {
			listener.moveToLeftForward();
		}
	}

	@Override
	public void moveToRightForward() {
		for (MotionListener listener : mMotionListeners) {
			listener.moveToRightForward();
		}
	}

	@Override
	public void moveToLeftBackward() {
		for (MotionListener listener : mMotionListeners) {
			listener.moveToLeftBackward();
		}
	}

	@Override
	public void moveToRightBackward() {
		for (MotionListener listener : mMotionListeners) {
			listener.moveToRightBackward();
		}
	}

	@Override
	public void rotateLeft() {
		for (MotionListener listener : mMotionListeners) {
			listener.rotateLeft();
		}
	}

	@Override
	public void rotateRight() {
		for (MotionListener listener : mMotionListeners) {
			listener.rotateRight();
		}
	}

	@Override
	public void stop() {
		for (MotionListener listener : mMotionListeners) {
			listener.stop();
		}
	}
}