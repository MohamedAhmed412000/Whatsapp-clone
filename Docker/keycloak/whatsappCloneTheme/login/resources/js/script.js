const mediaServiceBasePath = "/api/v1/media"

const step1 = document.getElementById("step1");
const step2 = document.getElementById("step2");
const nextBtn = document.getElementById("nextBtn");
const backBtn = document.getElementById("backBtn");
const signupBtn = document.getElementById("signupBtn");
const imageUpload = document.getElementById("imageUpload");
const profileImage = document.getElementById("profileImage");
const imageUrlRef = document.getElementById("imageUrl");

let uploadedImageRef = null;
let uuid = null;

// When image is clicked, trigger file input
profileImage.addEventListener("click", () => imageUpload.click());

// Handle image upload
imageUpload.addEventListener("change", async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    function generateUUID() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            const r = Math.random() * 16 | 0;
            const v = c === 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }

    if (uuid == null) {
        uuid = generateUUID();
    }
    let mediaUrl = mediaServiceBaseUrl + mediaServiceBasePath;

    try {
        if (typeof uploadedImageRef !== "undefined" && uploadedImageRef) {
            const deleteUrl = `${mediaUrl}/${uploadedImageRef}`;
            const deleteResponse = await fetch(deleteUrl, {
                method: "DELETE",
            });
            if (!deleteResponse.ok) {
                console.warn("Failed to delete old image before uploading new one");
            } else {
                console.log("Previous image deleted successfully");
            }
        }

        const formData = new FormData();
        formData.append("files", file);
        formData.append("entityId", `USER_${uuid}`);
        formData.append("filePath", uuid);

        const uploadResponse = await fetch(mediaUrl, {
            method: "POST",
            body: formData
        });

        if (!uploadResponse.ok) throw new Error("Upload failed");

        const result = await uploadResponse.json();
        uploadedImageRef = result.mediaReferencesList[0];
        imageUrlRef.value = uploadedImageRef;

        const reader = new FileReader();
        reader.onload = (e) => profileImage.src = e.target.result;
        reader.readAsDataURL(file);

        console.log("Image uploaded successfully:", uploadedImageRef);

    } catch (error) {
        alert("Error uploading image");
        console.error(error);
    }
});

nextBtn.addEventListener("click", (e) => {
    e.preventDefault();
    const firstName = document.getElementById("firstName").value.trim();
    const lastName = document.getElementById("lastName").value.trim();

    if (!firstName || !lastName) {
        alert("Please enter both first name and last name before continuing.");
        return;
    }

    step1.classList.add("hidden");
    step2.classList.remove("hidden");
});

backBtn.addEventListener("click", () => {
    step2.classList.add("hidden");
    step1.classList.remove("hidden");
});

signupBtn.addEventListener("click", (e) => {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value;
    const confirm = document.getElementById("password-confirm").value;
    if (!email || !password || !confirm) {
        e.preventDefault();
        alert("Please fill all fields before signing up.");
        return;
    }
    if (password !== confirm) {
        e.preventDefault();
        alert("Passwords do not match.");
        return;
    }
    console.log("Submitting signup form...");
});