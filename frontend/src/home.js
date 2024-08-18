import React, { useEffect, useState } from 'react';

const Home = () => {
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  const fetchProtectedData = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/protected', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
  
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
  
      const textData = await response.text(); // 문자열 형식으로 응답 데이터 가져오기
      setData(textData); // 상태에 데이터 저장
  
    } catch (error) {
      console.error('There was a problem with your fetch operation:', error);
      setError(error.message); // 에러 메시지 저장
    }
  };

  useEffect(() => {
    fetchProtectedData(); // 컴포넌트가 마운트될 때 데이터 요청
  }, []);

  return (
    <div>
      <h1>Welcome to Home Page</h1>
      {error && <p>Error: {error}</p>} {/* 에러 메시지 표시 */}
      {data ? (
        <pre>{JSON.stringify(data, null, 2)}</pre> // 데이터가 있을 경우 출력
      ) : (
        <p>Loading...</p> // 데이터 로딩 중 표시
      )}
    </div>
  );
}

export default Home;
