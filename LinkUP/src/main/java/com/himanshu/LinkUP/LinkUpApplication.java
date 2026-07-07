package com.himanshu.LinkUP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LinkUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkUpApplication.class, args);
	}

}

/*
                                        Profile Image Upload WorkFlow

On LinkedIn
Edit Profile -> Upload Profile Picture -> Choose image.jpg -> click Save

Now the BackedEnd Should
Receive the image -> Validate the Image -> Rename Image -> Store Image -> Save Image Path in DataBase -> Return success

WE need to rename the image
since if the first user Uploads profile.jpg
and the second user uploads the profile.jpg then  the second overwrites the first very badly

Hence for this prevension we will be using the UUID for this Universal Unique Identifier
since for every file it generates a random thing like 550e8400-e29b-41d4-a716-446655440000
which is stored as a fileName becomes: 550e8400-profile.jpg



MultipartFile -> normally the frontend sends the JSON
but the image is not the JSON , its a binary data
So Spring Provides: MultipartFile
It is a kind of wrapper around an uploaded file

So this MultipartFile file -> every thing about the uploaded file is present inside this object
contains the information Like
ImageName -> profile.jpg
ImageSize -> 2.4MB
ImageType -> image/jpeg
ImageBytes -> 0101010100111.......


Instead of sending the JSON we will now be sending the multipart/form-data
/api/users/profile-image
Body
file: image.png



Backend
@PostMapping -> MultipartFile path -> Java Object -> Save


Complete Flow: Frontend -> Choose File -> POST multipart/form-data -> SpringBoot -> MutlipartFile -> Validate -> Generate UUID -> Save file -> Store Path -> Update User -> Return Success

 */
