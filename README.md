**Describe the bug**
After the fragment is destroyed the application does not properly destroy the instances causing a leak (can cause an `OutOfMemoryError`).

**Which version?**
v5.2.42043.1112

**To Reproduce**
Steps to reproduce the behavior:
1. Copy and paste ZoomVideocall.java inside your application
2. Inside a fragment through any ui interface (like a button) start the class using the following functions:
```
	private ZoomVideocall mVideocall;

    private void startVideocall() {
        if(mVideocall == null) {
            mVideocall = new ZoomVideocall(requireContext());
        }

        mVideocall.start();
    }
```
3. Add `onDestroyView()`
```
	@Override
    public void onDestroyView() {
        super.onDestroyView();
        mVideocall.destroy();
        ...
    }
```
4. Now check with LeakCanary the leak produced.

**Expected behavior**
Should not leak.

**Files**
dump.txt
ZoomVideocall.java

**Smartphone (please complete the following information):**
 - Device: OnePlus Nord

**Additional context**
 - Build.VERSION.SDK_INT: 29
 - LeakCanary version: 2.5
