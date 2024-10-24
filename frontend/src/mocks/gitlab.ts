// import { rest } from 'msw';
// import { GitlabProjectListResponse } from 'types/gitLab';
// import { UpdateRepositoryRequest } from 'types/api';

// export const handlers = [
//   rest.get('/', (req, res, ctx) => {
//     return res(
//       ctx.status(200),
//       ctx.json({
//         message: 'Welcome to the API!',
//       }),
//     );
//   }),

//   rest.get('/api/link/search', (req, res, ctx) => {
//     const keyword = req.url.searchParams.get('keyword');
//     const page = req.url.searchParams.get('page');
//     const size = req.url.searchParams.get('size');

//     console.log('Keyword:', keyword);
//     console.log('Page:', page);
//     console.log('Size:', size);

//     if (!keyword || !page || !size) {
//       return res(
//         ctx.status(400),
//         ctx.json({
//           status: 'ERROR',
//           httpStatus: 'BAD_REQUEST',
//           code: 'ERR001',
//           message: 'Missing required query parameters',
//         }),
//       );
//     }

//     const responseData: GitlabProjectListResponse = {
//       gitlabProjectList: Array.from({ length: 101 }, (v, i) => ({
//         gitlabProjectId: 10 + i,
//         name: `Project ${String.fromCharCode(65 + i)}`,
//         isLinked: false,
//         isLinkable: i % 2 === 0,
//       })),
//       totalPages: 11,
//       totalElements: 101,
//       isLast: false,
//       currPage: 1,
//     };

//     return res(
//       ctx.status(200),
//       ctx.json({
//         status: 'SUCCESS',
//         data: responseData,
//       }),
//     );
//   }),
//   rest.post<UpdateRepositoryRequest>('/api/link/repository/:repoId/update', (req, res, ctx) => {
//     const { repoId } = req.params;
//     const { token } = req.body;

//     if (!token) {
//       return res(
//         ctx.status(400),
//         ctx.json({
//           status: 'ERROR',
//           message: '토큰이 필요합니다.',
//         }),
//       );
//     }

//     return res(
//       ctx.status(200),
//       ctx.json({
//         status: 'SUCCESS',
//         message: `리포지토리 ${repoId}의 토큰이 업데이트되었습니다.`,
//       }),
//     );
//   }),
// ];
