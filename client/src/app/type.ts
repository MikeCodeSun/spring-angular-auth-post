export interface RegisterErr {
  username: string;
  password: string;
}
export interface Post {
  content: string;
  createdAt: string;
  id: number;
  user: User;
  updatedAt?: string;
}

export interface User {
  id: number;
  username: string;
  image?: string;
  followers: number;
  following: number;
  follow: boolean;
}
